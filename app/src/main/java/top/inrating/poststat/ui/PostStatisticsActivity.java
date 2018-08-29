package top.inrating.poststat.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import top.inrating.poststat.R;
import top.inrating.poststat.databinding.PostStatisticsActivityBinding;
import top.inrating.poststat.viewmodel.PostStatisticsViewModel;

/**
 *
 *
 *
 */

public class PostStatisticsActivity extends AppCompatActivity implements
        IPostStatisticsActivity {

    public static final String ARG_POST_ID = "post_id";
    private static final String KEY_LOADING_STATE = "loading_state";
    private PostStatisticsActivityBinding mBinding;
    private PostStatisticsViewModel mViewModel;
    private int mPostId;
    private int mCurrentBtmMenuItemIndex;
    private TabLayout mTabLayout;
    private BtmNavFragPagerAdapter mBtmNavFragPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.post_statistics_activity);
        //mBinding = PostStatisticsActivityBinding.inflate(getLayoutInflater());
        mBinding = DataBindingUtil.setContentView(this, R.layout.post_statistics_activity);
        mBinding.setRefreshpoststat(mRefreshPostStatisticssInitCallback);
        mBinding.setBacknavigation(mBackNavigationInitCallback);

        // Add statistics list fragment if this is first creation
        if (savedInstanceState == null) {
            mBinding.setLoadingState(0);
            PostStatisticsListFragment fragment = new PostStatisticsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, PostStatisticsListFragment.TAG).commit();
        } else {
            int stateSaved = savedInstanceState.getInt(KEY_LOADING_STATE, 0);
            Log.v("test", "loading state saved "+stateSaved);
            mBinding.setLoadingState(savedInstanceState.getInt(KEY_LOADING_STATE, 0));
        }

        // Bottom navigation menu
        createBottomNavigationMenu();

        mPostId = getIntent().getIntExtra(PostStatisticsActivity.ARG_POST_ID, 0);

        mViewModel = ViewModelProviders.of(this).get(PostStatisticsViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLoadingStatusChange(int status) {
        Log.v("test", "loading status changes to "+status);
        mBinding.setLoadingState(status);
    }

    private void createBottomNavigationMenu() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mCurrentBtmMenuItemIndex = 0;
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mBtmNavFragPagerAdapter =
                new BtmNavFragPagerAdapter(getSupportFragmentManager(),
                        PostStatisticsActivity.this, mCurrentBtmMenuItemIndex, mTabLayout);
        viewPager.setAdapter(mBtmNavFragPagerAdapter);
        mTabLayout.setupWithViewPager(viewPager);
        //initBtmMenuItems(mTabLayout, mBtmNavFragPagerAdapter, mCurrentBtmMenuItemIndex);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mTabLayout == null) return;
                //updateBtmMenuItem(mTabLayout, mCurrentBtmMenuItemIndex, 0);
                mBtmNavFragPagerAdapter.setCurrItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mCurrentBtmMenuItemIndex);
        mBtmNavFragPagerAdapter.initBtmMenu();
    }

    public class BtmNavFragPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 9;
        private String tabTitles[];
        private TypedArray imageResId = null;;
        private Context context;
        private Resources resources;
        private int currItem;
        private TabLayout tabLayout;

        public BtmNavFragPagerAdapter(FragmentManager fm, Context context,
                                      int currItem, TabLayout tabLayout) {
            super(fm);
            this.context = context;
            this.resources = context.getResources();
            imageResId = resources.obtainTypedArray(R.array.statistics_actions_icons);
            tabTitles = resources.getStringArray(R.array.statistics_actions_texts);
            this.currItem = currItem;
            this.tabLayout = tabLayout;
            //initBtmMenu();
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return AuxPageFragment.newInstance(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            return v;
        }

        public void clean() {
            if (imageResId != null) imageResId.recycle();
            context = null;
            resources = null;
            imageResId = null;
            tabTitles = null;
            tabLayout = null;
        }

        public void initBtmMenu() {
            // Iterate over all tabs and set the custom view
            View v;
            ImageView iv;
            TextView tv;
            Log.v("test", "init btm menu, items amount="+tabLayout.getTabCount());
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
                iv = v.findViewById(R.id.imageview);
                tv = v.findViewById(R.id.textview);
                tab.setCustomView(v);
                if (i != currItem) {
                    iv.setImageResource(imageResId.getResourceId(i * 2, 0));
                } else {
                    iv.setImageResource(imageResId.getResourceId(i *2 + 1, 0));
                }
            }
        }

        private void updateBtmMenuItem(int itemIndex, int itemState) {
            TabLayout.Tab tab = tabLayout.getTabAt(itemIndex);
            View v = tab.getCustomView();
            ImageView iv = v.findViewById(R.id.imageview);
            TextView tv = v.findViewById(R.id.textview);
            if (itemState == 0) {
                iv.setImageResource(imageResId.getResourceId(itemIndex * 2, 0));
            } else {
                iv.setImageResource(imageResId.getResourceId(itemIndex * 2 + 1, 0));
            }
        }

        public void setCurrItem(int currItem) {
            updateBtmMenuItem(this.currItem, 0);
            this.currItem = currItem;
            updateBtmMenuItem(this.currItem, 1);
        }
    }

    private final RefreshPostStatisticssInitCallback mRefreshPostStatisticssInitCallback =
            new RefreshPostStatisticssInitCallback() {
                @Override
                public void onInit(View view) {
                    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        /*
                        final float DISABLE_ALPHA = 0.2f;
                        float alpha = ((ImageView) view).getAlpha();
                        if (alpha == DISABLE_ALPHA) return;
                        view.setAlpha(DISABLE_ALPHA);
                        */
                        if (mBinding.getLoadingState()==2) { // список статистик отображён
                            mBinding.setLoadingState(1);     // updating list mode
                        } else {
                            mBinding.setLoadingState(0);     // loading list mode on empty screen
                        }
                        Log.v("test", "set loading state to "+mBinding.getLoadingState());
                        mViewModel.refreshPostStatisticss(mPostId, mBinding.getLoadingState());
                    }
                }
            };

    private final BackNavigationInitCallback mBackNavigationInitCallback =
            new BackNavigationInitCallback() {
                @Override
                public void onInit(View view) {
                    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        finish();
                    }
                }
            };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LOADING_STATE, mBinding.getLoadingState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (mBtmNavFragPagerAdapter != null) {
            mBtmNavFragPagerAdapter.clean();
        }
        super.onDestroy();

    }

}
