package com.m3gv.news.business.video;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.m3gv.news.R;
import com.m3gv.news.common.videoplayer.VideoDurationEntity;
import com.m3gv.news.common.videoplayer.VideoEntity;
import com.m3gv.news.common.videoplayer.VideoPlayerActivity;
import com.m3gv.news.common.videoplayer.VideoPlayerFragment;

/**
 * Created by meikai on 16/12/3.
 */

public class VideoDetailActivity extends VideoPlayerActivity implements View.OnClickListener {

    private static String KEY_VIDEO_NEWS_ENTITY = "key_video_news_entity";


    private TextView tvVideoTitle;
    private ImageView imgvZan;
    private ImageView imgvCai;

    private VideoNewsEntity videoNewsEntity;

    public static void start(Activity activity, VideoNewsEntity videoNewsEntity) {
        Intent intent = new Intent(activity, VideoDetailActivity.class);
        intent.putExtra(KEY_VIDEO_NEWS_ENTITY, videoNewsEntity);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_detail_activity);

        videoNewsEntity = getIntent().getParcelableExtra(KEY_VIDEO_NEWS_ENTITY);

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.title = videoNewsEntity.videoTitle;
        videoEntity.url = videoNewsEntity.videoUrl;
        videoEntity.durationEntity = new VideoDurationEntity(videoNewsEntity.videoDuration);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_video_container,
                VideoPlayerFragment.newInstance(videoEntity))
                .commitAllowingStateLoss();

        tvVideoTitle = f(R.id.tv_video_detail_title);
        tvVideoTitle.setText(videoEntity.title);

        imgvZan = (ImageView) findViewById(R.id.img_zan);
        imgvCai = (ImageView) findViewById(R.id.img_cai);

        imgvZan.setOnClickListener(this);
        imgvCai.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_zan: {
                PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.5f, 1.0f);
                PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.5f, 1.0f);
                ObjectAnimator objectAnimator = ObjectAnimator
                        .ofPropertyValuesHolder(imgvZan, valuesHolder, valuesHolder1);
                objectAnimator.setDuration(300);
                objectAnimator.start();
            }
            break;
            case R.id.img_cai: {
                PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.5f, 1.0f);
                PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.5f, 1.0f);
                ObjectAnimator objectAnimator = ObjectAnimator
                        .ofPropertyValuesHolder(imgvCai, valuesHolder, valuesHolder1);
                objectAnimator.setDuration(300);
                objectAnimator.start();
            }
            break;
            default:
                break;
        }

    }
}
