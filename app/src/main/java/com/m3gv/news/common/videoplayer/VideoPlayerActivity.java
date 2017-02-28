package com.m3gv.news.common.videoplayer;

import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.videoplayer.exoplayer.ExoVideoPlayerFragment;

import java.lang.ref.WeakReference;

/**
 * Created by meikai on 16/12/14.
 */
public class VideoPlayerActivity extends M3gBaseActivity {

    private WeakReference<ExoVideoPlayerFragment> videoPlayerFragmentRef;

    public void bindVideoPlayerFragment(ExoVideoPlayerFragment videoPlayerFragment) {
        if (videoPlayerFragmentRef == null || videoPlayerFragmentRef.get() != videoPlayerFragment) {
            videoPlayerFragmentRef = new WeakReference<>(videoPlayerFragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (videoPlayerFragmentRef != null) {
            ExoVideoPlayerFragment videoPlayerFragment = videoPlayerFragmentRef.get();
            if (videoPlayerFragment != null) {
                if (videoPlayerFragment.onBackPressed()) {
                    return;
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    /**
     * 根据 设置的屏幕状态 来设置 Activity的窗口属性
     *
     * @param newFullScreenState 期望设置的 屏幕状态
     */
    public void onFullScreenChange(boolean newFullScreenState) {
        if (newFullScreenState) {
            //设置为 横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(params);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            //设置为 竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(params);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

}
