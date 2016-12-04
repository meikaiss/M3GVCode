package com.m3gv.news.business.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;

/**
 * Created by meikai on 16/12/4.
 */

public class VideoListActivity extends M3gBaseActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, VideoListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_activity);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, VideoListFragment.newInstance())
                .commit();

    }
}
