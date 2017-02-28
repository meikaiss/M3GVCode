package com.m3gv.news.common.videoplayer.exoplayer;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.github.rubensousa.previewseekbar.PreviewSeekBar;
import com.github.rubensousa.previewseekbar.PreviewSeekBarLayout;
import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.common.videoplayer.VideoEntity;
import com.m3gv.news.common.videoplayer.VideoPlayerActivity;

/**
 * Created by meikai on 17/2/15.
 */

public class ExoVideoPlayerFragment extends M3gBaseFragment {

    private static String KEY_VIDEO_ENTITY = "key_video_entity";

    private ExoPlayerManager exoPlayerManager;
    private PreviewSeekBar seekBar;

    private ImageButton fullscreenImgBtn;

    private boolean isInFullScreen;

    public static ExoVideoPlayerFragment newInstance(VideoEntity videoEntity) {
        ExoVideoPlayerFragment exoVideoPlayerFragment = new ExoVideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_VIDEO_ENTITY, videoEntity);
        exoVideoPlayerFragment.setArguments(bundle);

        return exoVideoPlayerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.exo_video_player_fragment, container, false);

        rootView.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        rootView.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels * 9 / 16;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SimpleExoPlayerView playerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);
        seekBar = (PreviewSeekBar) playerView.findViewById(R.id.exo_progress);

        seekBar.addOnSeekBarChangeListener(onSeekBarChangeListener);

        fullscreenImgBtn = f(view, R.id.imgb_fullscreen);
        fullscreenImgBtn.setOnClickListener(clickListener);

        String videoUrl = "http://ac-vkbqghtr.clouddn.com/86760493b02a6307f8ed.mp4";
        exoPlayerManager = new ExoPlayerManager(playerView, videoUrl);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((VideoPlayerActivity) getActivity()).bindVideoPlayerFragment(this);
    }

    public boolean onBackPressed() {
        if (isInFullScreen) {
            onFullScreenToggle();
            return true;
        }
        return false;
    }

    /**
     * 自动切换 横竖屏
     * 若当前为竖屏，则切换到横屏；若当前为横屏，则切换到竖屏
     */
    private void onFullScreenToggle() {
        boolean newFullScreenState = isInFullScreen = !isInFullScreen;
        if (newFullScreenState) {
            // 期望到横屏，但当前是竖屏
            fullscreenImgBtn.setBackgroundResource(R.drawable.video_player_fullscreen_landscape_selector);
            rootView.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().heightPixels;
            rootView.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels;
        } else {
            // 期望到竖屏，但当前是横屏，注意此时getDisplayMetrics()方法得到的宽高也是横屏时宽高!
            fullscreenImgBtn.setBackgroundResource(R.drawable.video_player_fullscreen_portrait_selector);
            rootView.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().heightPixels;
            rootView.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().heightPixels * 9 / 16;
        }
        ((VideoPlayerActivity) getActivity()).onFullScreenChange(isInFullScreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        exoPlayerManager.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        exoPlayerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayerManager.onStop();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgb_fullscreen:
                    if (getActivity() != null && getActivity() instanceof VideoPlayerActivity) {
                        onFullScreenToggle();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
