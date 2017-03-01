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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.m3gv.news.R;
import com.m3gv.news.common.util.UIUtil;
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
        tvZanCount = f(R.id.tv_zan_count);
        tvCaiCount = f(R.id.tv_cai_count);

        tvZanCount.setText(videoNewsEntity.zanCount + "");
        tvCaiCount.setText(videoNewsEntity.caiCount + "");

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
                zanClick();
                break;
            case R.id.layout_cai:
                caiClick();
                break;
            default:
                break;
        }
    }

    private void zanClick() {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.5f, 1.0f);
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.5f, 1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofPropertyValuesHolder(imgvZan, valuesHolder, valuesHolder1);
        objectAnimator.setDuration(300);
        objectAnimator.start();

        videoNewsEntity.avObject.increment("zanCount");
        videoNewsEntity.avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    UIUtil.showToast("赞+1");
                    videoNewsEntity.zanCount++;
                    tvZanCount.setText(videoNewsEntity.zanCount + "");
                } else {
                    UIUtil.showToast(e.getMessage());
                }
            }
        });
    }

    private void caiClick() {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.5f, 1.0f);
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.5f, 1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofPropertyValuesHolder(imgvCai, valuesHolder, valuesHolder1);
        objectAnimator.setDuration(300);
        objectAnimator.start();

        videoNewsEntity.avObject.increment("caiCount");
        videoNewsEntity.avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    UIUtil.showToast("踩+1");
                    videoNewsEntity.caiCount++;
                    tvCaiCount.setText(videoNewsEntity.caiCount + "");
                } else {
                    UIUtil.showToast(e.getMessage());
                }
            }
        });
    }

}
