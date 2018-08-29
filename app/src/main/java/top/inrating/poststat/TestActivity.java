package top.inrating.poststat;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.generated.callback.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import top.inrating.poststat.databinding.PostStatisticsActivityBinding;
import top.inrating.poststat.ui.AuxPageFragment;
import top.inrating.poststat.ui.PostStatisticsActivity;
import top.inrating.poststat.ui.PostStatisticsListFragment;
import top.inrating.poststat.ui.RefreshPostStatisticssInitCallback;

/**
 * Created by alexandr on 11.12.17.
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);


        final EditText etPostId = (EditText) findViewById(R.id.edittext_post_id);
        Button btnShowStats = (Button) findViewById(R.id.btn_show_stats);
        btnShowStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this,
                        top.inrating.poststat.ui.PostStatisticsActivity.class);
                String postId = etPostId.getText().toString();
                if (postId.length()>0) intent.putExtra(PostStatisticsActivity.ARG_POST_ID, Integer.valueOf(postId));
                startActivity(intent);
            }
        });

    }
}