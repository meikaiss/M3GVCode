package com.m3gv.news.common.videoplayer;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.common.util.TimeUtil;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by meikai on 16/12/3.
 */
public class VideoPlayerFragment extends M3gBaseFragment {

    private static int ANIMATOR_DURATION_CONTROLLER_BAR = 300;
    private static String KEY_VIDEO_ENTITY = "key_video_entity";

    private int playType = TXLivePlayer.PLAY_TYPE_VOD_MP4;

    private TXLivePlayer livePlayer;
    private TXLivePlayConfig playConfig;

    private FrameLayout realVideoContainer;
    private TXCloudVideoView playerView;
    private ProgressBar progressBarLoading;
    private TextView tvRePlay;
    private LinearLayout videoPlayerControllerBar;
    /**
     * 必须使用 视频的时长（单位：秒）来作为进度条的数据
     */
    private ImageView playOrPauseImgv;
    private SeekBar seekBar;
    private TextView tvCurrent;
    private TextView tvTotal;
    private ImageButton fullscreenImgBtn;

    private boolean isControllerBarShowing = true;
    private boolean isVideoPlaying;
    private boolean isSeekBarInDrag;
    private boolean isInFullScreen;
    private VideoEntity videoEntity;

    public static VideoPlayerFragment newInstance(VideoEntity videoEntity) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_VIDEO_ENTITY, videoEntity);
        videoPlayerFragment.setArguments(bundle);

        return videoPlayerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.video_player_fragment, container, false);

        rootView.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        rootView.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels * 9 / 16;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        livePlayer = new TXLivePlayer(getContext());
        livePlayer.setPlayListener(itxLivePlayListener);
        playConfig = new TXLivePlayConfig();
        livePlayer.setConfig(playConfig);

        realVideoContainer = f(view, R.id.real_video_container);
        realVideoContainer.setOnClickListener(clickListener);
        videoPlayerControllerBar = f(view, R.id.video_player_controller_bar);
        playOrPauseImgv = f(view, R.id.play_or_pause);
        playOrPauseImgv.setOnClickListener(clickListener);
        playerView = f(rootView, R.id.m3_video_view);
        progressBarLoading = f(view, R.id.progress_video_loading);
        tvRePlay = f(view, R.id.tv_re_play);
        tvRePlay.setOnClickListener(clickListener);
        seekBar = f(view, R.id.controller_seek_bar);
        tvCurrent = f(view, R.id.time_current);
        tvTotal = f(view, R.id.time_total);
        fullscreenImgBtn = f(view, R.id.fullscreen);
        fullscreenImgBtn.setOnClickListener(clickListener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarInDrag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarInDrag = false;
                if (livePlayer != null) {
                    livePlayer.seek(seekBar.getProgress());
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((VideoPlayerActivity) getActivity()).bindVideoPlayerFragment(this);

        Bundle paramsBundle = getArguments();
        if (paramsBundle != null) {
            videoEntity = paramsBundle.getParcelable(KEY_VIDEO_ENTITY);
        }

        if (videoEntity != null) {
            seekBar.setMax(videoEntity.durationEntity.totalSecond);
            tvTotal.setText(videoEntity.durationEntity.toString());

            // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
            int result = livePlayer.startPlay(videoEntity.url, playType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeVideo();
    }

    private void resumeVideo() {
        if (livePlayer != null && playerView != null) {
            livePlayer.resume();
            playOrPauseImgv.setBackgroundResource(R.drawable.video_player_pause_icon_selector);
            isVideoPlaying = true;
            playerView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseVideo();
    }

    private void pauseVideo() {
        if (livePlayer != null && playerView != null) {
            livePlayer.pause();
            playerView.onPause();
            playOrPauseImgv.setBackgroundResource(R.drawable.video_player_play_icon_selector);
            isVideoPlaying = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (livePlayer != null) {
            livePlayer.stopPlay(true);
        }
        if (playerView != null) {
            playerView.onDestroy();
        }
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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.real_video_container:
                    if (isControllerBarShowing) {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(videoPlayerControllerBar, "translationY", 0,
                                videoPlayerControllerBar.getMeasuredHeight())
                                .setDuration(ANIMATOR_DURATION_CONTROLLER_BAR);
                        animator.start();
                        isControllerBarShowing = false;
                    } else {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(videoPlayerControllerBar, "translationY",
                                videoPlayerControllerBar.getMeasuredHeight(), 0)
                                .setDuration(ANIMATOR_DURATION_CONTROLLER_BAR);
                        animator.start();
                        isControllerBarShowing = true;
                    }
                    break;
                case R.id.play_or_pause:
                    if (isVideoPlaying) {
                        pauseVideo();
                    } else {
                        resumeVideo();
                    }
                    break;
                case R.id.fullscreen:
                    if (getActivity() != null && getActivity() instanceof VideoPlayerActivity) {
                        onFullScreenToggle();
                    }
                    break;
                case R.id.tv_re_play:
                    playOrPauseImgv.callOnClick();
                    tvRePlay.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    private ITXLivePlayListener itxLivePlayListener = new ITXLivePlayListener() {
        @Override
        public void onPlayEvent(int event, Bundle bundle) {
            if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                progressBarLoading.setVisibility(View.GONE);
            } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                progressBarLoading.setVisibility(View.VISIBLE);
            } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                if (!isVideoPlaying) {
                    return;
                }
                int progress = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                int totalSecondInServerVideo = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);

                if (seekBar.getMax() == totalSecondInServerVideo) {
                    seekBar.setMax(totalSecondInServerVideo);
                }

                if (videoEntity.durationEntity.totalSecond != totalSecondInServerVideo) {
                    videoEntity.durationEntity.updateDuration(totalSecondInServerVideo);
                    tvTotal.setText(videoEntity.durationEntity.toString());
                }

                if (isSeekBarInDrag) {

                } else {
                    seekBar.setProgress(progress);
                }
                tvCurrent.setText(TimeUtil.toDurationString(progress));
            } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                pauseVideo();
            } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                seekBar.setProgress(0);
                tvCurrent.setText(TimeUtil.toDurationString(0));
                pauseVideo();
                tvRePlay.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onNetStatus(Bundle status) {
            Log.d("status", "Current status, CPU:" + status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE) +
                    ", RES:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) + "*" + status
                    .getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT) +
                    ", SPD:" + status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED) + "Kbps" +
                    ", FPS:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS) +
                    ", ARA:" + status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE) + "Kbps" +
                    ", VRA:" + status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE) + "Kbps");
        }
    };
}
