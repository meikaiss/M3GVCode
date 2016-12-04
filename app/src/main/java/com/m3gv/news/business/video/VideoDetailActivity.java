package com.m3gv.news.business.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.business.homepage.VideoNewsEntity;
import com.m3gv.news.common.videoplayer.VideoDurationEntity;
import com.m3gv.news.common.videoplayer.VideoEntity;
import com.m3gv.news.common.videoplayer.VideoPlayerFragment;

/**
 * Created by meikai on 16/12/3.
 */

public class VideoDetailActivity extends M3gBaseActivity {

    private static String KEY_VIDEO_NEWS_ENTITY = "key_video_news_entity";


    private TextView tvVideoTitle;

    public static void start(Activity activity, VideoNewsEntity videoNewsEntity) {
        Intent intent = new Intent(activity, VideoDetailActivity.class);
        intent.putExtra(KEY_VIDEO_NEWS_ENTITY, videoNewsEntity);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_detail_activity);

        VideoNewsEntity videoNewsEntity = getIntent().getParcelableExtra(KEY_VIDEO_NEWS_ENTITY);

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.title = videoNewsEntity.videoTitle;
        videoEntity.url = videoNewsEntity.videoUrl;
        videoEntity.durationEntity = new VideoDurationEntity(videoNewsEntity.videoDuration);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_video_container,
                VideoPlayerFragment.newInstance(videoEntity))
                .commitAllowingStateLoss();

        tvVideoTitle = f(R.id.tv_video_detail_title);
        tvVideoTitle.setText(videoEntity.title);
    }


}
