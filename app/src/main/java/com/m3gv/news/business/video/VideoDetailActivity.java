package com.m3gv.news.business.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.videoplayer.VideoDurationEntity;
import com.m3gv.news.common.videoplayer.VideoEntity;
import com.m3gv.news.common.videoplayer.VideoPlayerFragment;

/**
 * Created by meikai on 16/12/3.
 */

public class VideoDetailActivity extends M3gBaseActivity {


    private TextView tvVideoTitle;

    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoDetailActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_detail_activity);

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.title = "EP499魏延一刀五杀最心机借刀杀人！";
        videoEntity.url = "http://ac-orciop2a.clouddn.com/c0c422c71c897ece20d3.mp4";
        videoEntity.durationEntity = new VideoDurationEntity(0, 9, 27);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_video_container,
                VideoPlayerFragment.newInstance(videoEntity))
                .commitAllowingStateLoss();

        tvVideoTitle = f(R.id.tv_video_detail_title);
        tvVideoTitle.setText(videoEntity.title);
    }


}
