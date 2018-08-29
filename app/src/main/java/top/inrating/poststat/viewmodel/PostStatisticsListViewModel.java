package top.inrating.poststat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import top.inrating.poststat.App;
import top.inrating.poststat.db.entity.PostStatisticsEntity;

public class PostStatisticsListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<PostStatisticsEntity>> mObservablePostStatisticss;
    private Application mApplication;

    public PostStatisticsListViewModel(Application application) {
        super(application);

        mObservablePostStatisticss = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservablePostStatisticss.setValue(null);

        mApplication = application;
        /*
        LiveData<List<PostStatisticsEntity>> postStatisticss =
                ((top.inrating.poststat.App) application).getRepository()
                .getPostStatisticss(postId);

        // observe the changes of the post statistics from the database and forward them
        mObservablePostStatisticss.addSource(postStatisticss, mObservablePostStatisticss::setValue);
        */
    }

    /**
     * Expose the LiveData PostStatisticss query so the UI can observe it.
     */
    public LiveData<List<PostStatisticsEntity>> getPostStatisticss(final int postId) {

        LiveData<List<PostStatisticsEntity>> postStatisticss =
                ((App) mApplication).getRepository()
                        .getPostStatisticss(postId);

        // observe the changes of the post statistics from the database and forward them
        mObservablePostStatisticss.addSource(postStatisticss, mObservablePostStatisticss::setValue);

        return mObservablePostStatisticss;
    }

    public void onLoadingErrorAlertDismiss(int loadingState, int postId) {
        if (loadingState == -1) { // inet is not available
            ((App) mApplication).getRepository()
                    .updateStatisticssLoadingStatus(postId, 3);
        }
    }

}
