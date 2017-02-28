/*
 * Copyright 2016 The Android Open Source Project
 * Copyright 2017 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m3gv.news.common.videoplayer.exoplayer;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
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
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

import com.github.rubensousa.previewseekbar.PreviewSeekBarLayout;

public class ExoPlayerManager implements ExoPlayer.EventListener {

    private ExoPlayerMediaSourceBuilder mediaSourceBuilder;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;

    public ExoPlayerManager(SimpleExoPlayerView playerView, String url) {
        this.playerView = playerView;
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
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(playerView.getContext(),
                trackSelector, loadControl);
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
        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {

        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
