package com.m3gv.video.common.videoplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m3gv.video.R;
import com.m3gv.video.base.M3gBaseFragment;
import com.m3gv.video.common.util.StringUtil;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by meikai on 16/12/3.
 */
public class VideoPlayerFragment extends M3gBaseFragment {

    private static String KEY_VIDEO_URL = "key_video_url";

    private int playType = TXLivePlayer.PLAY_TYPE_VOD_MP4;

    private TXLivePlayer livePlayer = null;
    private TXLivePlayConfig playConfig;
    private TXCloudVideoView playerView;

    private String videoUrl;

    public static VideoPlayerFragment newInstance(String videoUrl) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_VIDEO_URL, videoUrl);
        videoPlayerFragment.setArguments(bundle);

        return videoPlayerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        livePlayer = new TXLivePlayer(getContext());
        livePlayer.setPlayListener(itxLivePlayListener);
        playConfig = new TXLivePlayConfig();
        livePlayer.setConfig(playConfig);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.video_player_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerView = (TXCloudVideoView) view.findViewById(R.id.m3_video_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle paramsBundle = getArguments();
        if (paramsBundle != null) {
            videoUrl = paramsBundle.getString(KEY_VIDEO_URL);
        }

        if (StringUtil.isNotEmpty(videoUrl)) {
            // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
            int result = livePlayer.startPlay("http://ac-orciop2a.clouddn.com/c0c422c71c897ece20d3.mp4", playType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (livePlayer != null) {
            livePlayer.resume();
        }
        if (playerView != null) {
            playerView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (livePlayer != null) {
            livePlayer.pause();
        }
        if (playerView != null) {
            playerView.onPause();
        }
    }

    private ITXLivePlayListener itxLivePlayListener = new ITXLivePlayListener() {
        @Override
        public void onPlayEvent(int i, Bundle bundle) {

        }

        @Override
        public void onNetStatus(Bundle bundle) {

        }
    };
}
