package com.m3gv.news.business.video;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.view.magicindicator.MagicIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */
public class VideoListFragment extends M3gBaseFragment {

    private ViewGroup emptyViewGroup;
    private ViewGroup loadingViewGroup;
    private ViewGroup noNetViewGroup;
    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;
    private XRecyclerView xRecyclerView;
    private TextView tvRefreshResultTip;

    private boolean isRefreshTipShow = false;
    private List<RefreshTipRunnable> refreshTipRunnableList = new ArrayList<>();

    private List<VideoNewsEntity> dataList = new ArrayList<>();
    private VideoNewsAdapter videoNewsAdapter;

    public static VideoListFragment newInstance() {
        Bundle args = new Bundle();

        VideoListFragment fragment = new VideoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.video_list_fragment, container, false);

        magicIndicator = f(R.id.magic_indicator);
        xRecyclerView = f(R.id.base_x_recycler_view);
        xRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        emptyViewGroup = f(R.id.empty_view);
        loadingViewGroup = f(R.id.loading_view);
        noNetViewGroup = f(R.id.net_error_view);
        tvRefreshResultTip = f(R.id.tv_refresh_result_tip);

        xRecyclerView.setVisibility(View.GONE);

        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        loadingViewGroup.setVisibility(View.VISIBLE);

        videoNewsAdapter = new VideoNewsAdapter(getActivity(), dataList, xRecyclerView.isPullRefreshEnabled());
        xRecyclerView.setAdapter(videoNewsAdapter);

        AVQuery<AVObject> avQuery = new AVQuery<>("VideoNews");
        avQuery.orderByAscending("videoId").whereEqualTo("enable", true);
        avQuery.limit(1);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (VideoListFragment.this == null
                        || VideoListFragment.this.isDestroyed()
                        || list == null) {
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    dataList.add(VideoNewsEntity.parse(list.get(i)));
                }

                videoNewsAdapter.notifyItemInserted(dataList.size());
                xRecyclerView.setVisibility(View.VISIBLE);
                loadingViewGroup.setVisibility(View.GONE);
            }
        });

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                AVQuery<AVObject> avQuery = new AVQuery<>("VideoNews");
                avQuery.orderByAscending("videoId").whereEqualTo("enable", true);
                if (CollectionUtil.isNotEmpty(dataList)) {
                    avQuery.whereGreaterThan("videoId", dataList.get(0).videoId);
                }
                avQuery.limit(1);
                avQuery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (VideoListFragment.this == null
                                || VideoListFragment.this.isDestroyed()
                                || CollectionUtil.isEmpty(list)) {
                            showRefreshTip("没有发现新视频");
                            xRecyclerView.refreshComplete();
                            return;
                        }

                        for (int i = list.size() - 1; i >= 0; i--) {
                            dataList.add(0, VideoNewsEntity.parse(list.get(i)));
                        }

                        videoNewsAdapter.notifyItemInserted(1);
                        xRecyclerView.refreshComplete();
                        showRefreshTip(
                                getString(R.string.x_recycler_view_refresh_tip, String.valueOf(list.size()), "视频"));
                    }
                });
            }

            @Override
            public void onLoadMore() {

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(false);
        commonNavigator.setScrollPivotX(0.65f);


    }

    private void showRefreshTip(String msg) {
        tvRefreshResultTip.setText(msg);
        showRefreshTipAnimator();
    }

    private void showRefreshTipAnimator() {
        if (!isRefreshTipShow) {
            int deltaY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, Resources.getSystem()
                    .getDisplayMetrics());
            ObjectAnimator animator = ObjectAnimator.ofFloat(tvRefreshResultTip, "translationY", -deltaY, 0)
                    .setDuration(200);

            // 终止 之前的所有 tip退出动画
            for (int i = 0; i < refreshTipRunnableList.size(); i++) {
                // 将还未启动的tip退出动画 置死
                refreshTipRunnableList.get(i).terminateFlag = true;

                // 将已经启动的tip退出动画 取消执行
                if (refreshTipRunnableList.get(i).hideRefreshTipAnimator != null &&
                        refreshTipRunnableList.get(i).hideRefreshTipAnimator.isStarted()) {
                    refreshTipRunnableList.get(i).hideRefreshTipAnimator.end();
                }
            }

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    RefreshTipRunnable hideRefreshTipRunnable = new RefreshTipRunnable() {
                        @Override
                        public void run() {
                            if (terminateFlag) {
                                // 已经被外部终止，则不再执行任何动作
                            } else {
                                int deltaY = (int) TypedValue
                                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, Resources.getSystem()
                                                .getDisplayMetrics());
                                hideRefreshTipAnimator = ObjectAnimator
                                        .ofFloat(tvRefreshResultTip, "translationY", 0, -deltaY)
                                        .setDuration(200);
                                hideRefreshTipAnimator.start();
                            }
                        }
                    };
                    refreshTipRunnableList.add(hideRefreshTipRunnable);
                    new Handler().postDelayed(hideRefreshTipRunnable, 2000);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.start();

        } else {

        }
    }

}
