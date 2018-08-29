package top.inrating.poststat.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import top.inrating.poststat.R;
import top.inrating.poststat.databinding.PostStatisticsListFragmentBinding;
import top.inrating.poststat.db.entity.PostStatisticsEntity;
import top.inrating.poststat.model.PostStatistics;
import top.inrating.poststat.viewmodel.PostStatisticsListViewModel;


public class PostStatisticsListFragment extends Fragment {

    public static final String TAG = "PostStatisticsListViewModel";

    private PostStatisticsAdapter mPostStatisticsAdapter;

    private PostStatisticsListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.post_statistics_list_fragment, container, false);

        mPostStatisticsAdapter = new PostStatisticsAdapter(mPostStatisticsClickCallback);
        mBinding.postStatisticsList.setAdapter(mPostStatisticsAdapter);
        mBinding.executePendingBindings();

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final PostStatisticsListViewModel viewModel =
                ViewModelProviders.of(this).get(PostStatisticsListViewModel.class);

        int postId = getActivity().getIntent().getIntExtra(PostStatisticsActivity.ARG_POST_ID, 0);
        subscribeUi(viewModel, postId);
    }

    private void subscribeUi(final PostStatisticsListViewModel viewModel, final int postId) {
        // Update the list when the data changes
        LiveData<List<PostStatisticsEntity>> livePostStatisticss =
                viewModel.getPostStatisticss(postId);
        livePostStatisticss.observe(this, new Observer<List<PostStatisticsEntity>>() {
            @Override
            public void onChanged(@Nullable List<PostStatisticsEntity> postStatisticss) {
                Log.d("test", "onChanged, post_id="+postId+", postStatistics is "+
                        (postStatisticss==null?"null":("not null, size="+postStatisticss.size()))+
                        (postStatisticss==null || postStatisticss.size()==0 ? "":", loading state="+
                                postStatisticss.get(0).getLoadingState()));
                int loadingState = 0;
                if (postStatisticss != null && postStatisticss.size() > 0) {
                    loadingState = postStatisticss.get(0).getLoadingState();
                    mBinding.setLoadingState(loadingState);
                    mPostStatisticsAdapter.setPostStatisticsList(postStatisticss);
                    updateActivityLoadingStatus(loadingState);

                } else {
                    mBinding.setLoadingState(0);
                    updateActivityLoadingStatus(0);
                }

                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();

                if (loadingState < 0) {
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        displayErrorAlert(loadingState, getActivity(), viewModel, postId);
                    }
                }
            }
        });

        if (livePostStatisticss.getValue() == null || livePostStatisticss.getValue().size() == 0) {
            Log.d("test", "onChanged, post_id="+postId+", loading state set to 0");
            mBinding.setLoadingState(0);
            updateActivityLoadingStatus(0);
        }
    }

    private void updateActivityLoadingStatus(int state) {
        if (getActivity()!=null && !getActivity().isFinishing()) {
            ((IPostStatisticsActivity) getActivity()).onLoadingStatusChange(state);
        }
    }

    private final PostStatisticsClickCallback mPostStatisticsClickCallback = new PostStatisticsClickCallback() {
        @Override
        public void onClick(PostStatistics statistics) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                //((PostStatisticsActivity) getActivity()).show(statistics);
            }
        }
    };

    public static void displayErrorAlert(final int state, Context context,
                                         final PostStatisticsListViewModel viewModel,
                                         final int postId) {
        Log.v("test", "display alert for state "+state);

        // If state < 0 then we got error and then we have to show alert
        int msgResId = 0;
        int titleResId = 0;
        int iconResId = 0;
        int posTextId = 0;
        final Runnable callbackOnAlertDismiss;
        if (state < 0) {
            switch (state) {
                case -1:  // inet is not available
                    msgResId = R.string.stats_refresh_failure_no_inet;
                    posTextId = R.string.ok;
                    callbackOnAlertDismiss = new Runnable() {
                        @Override
                        public void run() {
                            Log.v("test", "callback on alert dismiss run");
                            viewModel.onLoadingErrorAlertDismiss(state, postId);
                        }
                    };
                    break;
                default:
                    callbackOnAlertDismiss = null;
            }
        } else callbackOnAlertDismiss = null;
        if (msgResId != 0) {

            MaterialDialog.Builder builder =
                    new MaterialDialog.Builder(context)
                            .content(msgResId);
            if (posTextId != 0) builder = builder.positiveText(posTextId);
            if (titleResId != 0) builder = builder.title(titleResId);
            if (iconResId != 0) builder = builder.iconRes(iconResId);

            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog dialog, DialogAction which) {
                    dialog.dismiss();
                    if (callbackOnAlertDismiss != null) {
                        callbackOnAlertDismiss.run();
                    }
                }
            });

            MaterialDialog dialog = builder.build();
            dialog.setCancelable(false);
            dialog.show();

        }

    }

}
