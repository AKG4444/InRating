package top.inrating.poststat.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import top.inrating.poststat.App;


public class PostStatisticsViewModel extends AndroidViewModel {

    private Application mApplication;

    public PostStatisticsViewModel(Application application) {
        super(application);

        mApplication = application;

    }

    public void refreshPostStatisticss(int postId, int loadingState) {
        ((App) mApplication).getRepository()
                .refreshStatisticss(postId, loadingState, mApplication);
    }

}
