package top.inrating.poststat;

import android.content.res.Resources;

import top.inrating.poststat.db.AppDatabase;
import top.inrating.poststat.ui.PostStatisticsActivity;

/**
 * Android App class. Used for accessing singletons.
 */
public class App extends android.app.Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase(), this);
    }

    public AppExecutors getExecutors() {
        return mAppExecutors;
    }

}
