package top.inrating.poststat;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import top.inrating.poststat.db.AppDatabase;
import top.inrating.poststat.db.DataGenerator;
import top.inrating.poststat.db.entity.PostStatisticsEntity;
import top.inrating.poststat.inrating_api.WebService;
import top.inrating.poststat.utills.Common;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final Application mApplication;
    private final AppDatabase mDatabase;
    private MediatorLiveData<List<PostStatisticsEntity>> mObservablePostStatisticss;

    private int mCurrentLoadingTaskPostId;

    private DataRepository(final AppDatabase database,
                           final Application application) {
        mDatabase = database;
        mApplication = application;
        /*
        mObservablePostStatisticss = new MediatorLiveData<>();

        mObservablePostStatisticss.addSource(mDatabase.postStatisticsDao().loadAllPostStatisticss(),
                postStatisticsEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservablePostStatisticss.postValue(postStatisticsEntities);
                    }
                });
                */
        mCurrentLoadingTaskPostId = 0;
    }

    public static DataRepository getInstance(final AppDatabase database,
                                             final Application application) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database, application);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of post statistics from the database and get notified when the data changes.
     */
    public LiveData<List<PostStatisticsEntity>> getPostStatisticss(final int postId) {
        mObservablePostStatisticss = new MediatorLiveData<>();

        mObservablePostStatisticss.addSource(mDatabase.postStatisticsDao().loadAllPostStatisticss(postId),
                postStatisticsEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {

                        mObservablePostStatisticss.postValue(postStatisticsEntities);

                        if (postStatisticsEntities.size() == 0) { // Assume that this means there is now info about the post in the db yet
                            // Then we have to load the info from server:

                            // this is still suboptimal but better than before.
                            // a complete implementation must also handle the error cases.
                            /*
                            webservice.getPostStatistics(postId).enqueue(new Callback<PostStatistics>() {
                                @Override
                                public void onResponse(Call<PostStatistics> call, Response<PostStatistics> response) {
                                    data.setValue(response.body());
                                }
                            });
                                    */

                            refreshStatisticss(postId, 0, mApplication);

                        }
                    }
                });

        return mObservablePostStatisticss;
    }

    public LiveData<PostStatisticsEntity> loadPostStatistics(final int postId, int statisticsType) {
        return mDatabase.postStatisticsDao().loadPostStatistics(postId, statisticsType);
    }

    private static void addDelay() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ignored) {
        }
    }

    public void updateStatisticssLoadingStatus(final int postId, final int newLoadingStatus) {
        ((App) mApplication).getExecutors().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.postStatisticsDao().updateLoadingStateSync(newLoadingStatus, postId);
            }
        });
    }

    public void refreshStatisticss(final int postId, final int loadingState,
                                   Application application) {
        final AppExecutors executors;
        final Context context;
        Log.d("test", "refreshing statistics");

        if (mCurrentLoadingTaskPostId !=0 && mCurrentLoadingTaskPostId == postId) return; // to prevent form
                                                                                          // adding task on Live Data Objects
                                                                                          // recriation
        else mCurrentLoadingTaskPostId = postId;

        executors = ((App) application).getExecutors();
        context = application.getApplicationContext();

        new AsyncTask<Integer, Void, Void>() {

            @Override
            protected Void doInBackground(Integer... params) {
                //saveCallResult(response.body);

                //mDatabase.postStatisticsDao().updateLoadingStateSync(params[1], params[0]);

                // Add a delay to simulate a long-running operation
                //addDelay();

                if (mCurrentLoadingTaskPostId == 0) this.cancel(true);

                // Check if inet is available
                int msgId;
                if (!Common.isInetAvailable(context)) {
                    // We save error state in the db and this will cause alert dialog popping
                    mDatabase.postStatisticsDao().updateLoadingStateSync(-1, params[0]);


                } else {

                    mDatabase.postStatisticsDao().updateLoadingStateSync(params[1], params[0]);

                    // Get statistics from the server
                    List<PostStatisticsEntity> newPostStatisticss = WebService.getPostStatisticss(params[0]);


                    // Debugging mode:
                    // Generate the data for pre-population
                    //addDelay();
                    //List<PostStatisticsEntity> newPostStatisticss = DataGenerator.generatePostStatisticss(params[0]);


                    final List<PostStatisticsEntity> currentPostStatisticss =
                            mDatabase.postStatisticsDao().loadAllPostStatisticssSync(params[0]);
                    mDatabase.runInTransaction(() -> {
                        Log.d("test", "refreshing statistics, post id = " + params[0]);
                        if (currentPostStatisticss != null &&
                                currentPostStatisticss.size() > 0) {
                            Log.d("test", "refreshing statistics, deleting " +
                                    currentPostStatisticss.size() + " statistics");
                            mDatabase.postStatisticsDao().deleteAll(currentPostStatisticss);
                        }
                        mDatabase.postStatisticsDao().insertAll(newPostStatisticss);
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Log.d("test", "refreshing statistics, on post execute");
                mCurrentLoadingTaskPostId = 0;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, postId, loadingState);
    }
}
