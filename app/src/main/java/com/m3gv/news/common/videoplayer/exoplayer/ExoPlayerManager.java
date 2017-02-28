package com.m3gv.news.common.videoplayer.exoplayer;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import android.content.res.Resources;
import android.view.View;
import android.widget.ProgressBar;

public class ExoPlayerManager implements ExoPlayer.EventListener {

    private ExoPlayerMediaSourceBuilder mediaSourceBuilder;
    private SimpleExoPlayerView playerView;
    private ProgressBar circleProgressBar;
    private SimpleExoPlayer player;
    private View rootView;

    private boolean firstReady = true;

    public ExoPlayerManager(View rootView, SimpleExoPlayerView playerView, ProgressBar circleProgressBar, String url) {
        this.rootView = rootView;
        this.playerView = playerView;
        this.circleProgressBar = circleProgressBar;
        this.mediaSourceBuilder = new ExoPlayerMediaSourceBuilder(playerView.getContext(), url);
    }

    public void onStart() {
        if (Util.SDK_INT > 23) {
            createPlayers();
        }
    }

    public void onResume() {
        if ((Util.SDK_INT <= 23 || player == null)) {
            createPlayers();
        }
    }

    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayers();
        }
    }

    public void onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayers();
        }
    }

    private void releasePlayers() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void createPlayers() {
        player = createFullPlayer();
        playerView.setPlayer(player);
    }

    private SimpleExoPlayer createFullPlayer() {
        TrackSelection.Factory videoTrackSelectionFactory
                = new AdaptiveVideoTrackSelection.Factory(new DefaultBandwidthMeter());
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory
                .newSimpleInstance(playerView.getContext(), trackSelector, loadControl);
        player.setPlayWhenReady(true);
        player.prepare(mediaSourceBuilder.getMediaSource(false));
        player.addListener(this);
        return player;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_IDLE:

                break;
            case ExoPlayer.STATE_BUFFERING:
                circleProgressBar.setVisibility(View.VISIBLE);
                break;
            case ExoPlayer.STATE_READY:
                if (firstReady) {
                    setWidthHeight(player.getVideoFormat());
                    firstReady = false;
                }
                circleProgressBar.setVisibility(View.INVISIBLE);
                break;
            case ExoPlayer.STATE_ENDED:

                break;
            default:
                break;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private void setWidthHeight(Format format) {
        rootView.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        rootView.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels
                * format.height / format.width;
        rootView.setLayoutParams(rootView.getLayoutParams());
    }


}
