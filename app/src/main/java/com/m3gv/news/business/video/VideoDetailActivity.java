package com.m3gv.news.business.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.m3gv.news.R;
import com.m3gv.news.business.zanCai.ZanCaiVideoHelper;
import com.m3gv.news.common.videoplayer.VideoDurationEntity;
import com.m3gv.news.common.videoplayer.VideoEntity;
import com.m3gv.news.common.videoplayer.VideoPlayerActivity;
import com.m3gv.news.common.videoplayer.exoplayer.ExoVideoPlayerFragment;

/**
 * Created by meikai on 16/12/3.
 */
public class VideoDetailActivity extends VideoPlayerActivity implements View.OnClickListener {

    private static String KEY_VIDEO_NEWS_ENTITY = "key_video_news_entity";


    private TextView tvVideoTitle;
    private ImageView imgvZan;
    private ImageView imgvCai;
    private TextView tvZanCount;
    private TextView tvCaiCount;


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
                ExoVideoPlayerFragment.newInstance(videoEntity))
                .commitAllowingStateLoss();

        tvVideoTitle = f(R.id.tv_video_detail_title);
        tvVideoTitle.setText(videoEntity.title);

        imgvZan = f(R.id.img_zan);
        imgvCai = f(R.id.img_cai);
        tvZanCount = f(R.id.tv_zan_video_detail_count);
        tvCaiCount = f(R.id.tv_cai_video_detail_count);

        new ZanCaiVideoHelper().init(videoNewsEntity, tvZanCount, imgvZan, tvCaiCount, imgvCai);

        f(R.id.layout_zan).setOnClickListener(this);
        f(R.id.layout_cai).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_zan:
                new ZanCaiVideoHelper().zanClick("video", imgvZan, tvZanCount,
                        imgvCai, tvCaiCount, videoNewsEntity);
                break;
            case R.id.layout_cai:
                new ZanCaiVideoHelper().caiClick("video", imgvZan, tvZanCount,
                        imgvCai, tvCaiCount, videoNewsEntity);
                break;
            default:
                break;
        }
    }

}
