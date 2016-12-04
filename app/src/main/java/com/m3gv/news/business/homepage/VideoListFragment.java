package com.m3gv.news.business.homepage;

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
    private XRecyclerView xRecyclerView;
    private TextView tvRefreshResultTip;

    private boolean isRefreshTipShow = false;
    private ObjectAnimator hideRefreshTipAnimator;
    private boolean isGoing2HideRefreshTip = false;

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
        rootView = inflater.inflate(R.layout.home_main_activity, container, false);

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

        videoNewsAdapter = new VideoNewsAdapter(getActivity(), dataList);
        xRecyclerView.setAdapter(videoNewsAdapter);

        AVQuery<AVObject> avQuery = new AVQuery<>("VideoNews");
        avQuery.orderByAscending("updatedAt").whereEqualTo("enable", true);
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

                videoNewsAdapter.notifyDataSetChanged();
                xRecyclerView.setVisibility(View.VISIBLE);
                loadingViewGroup.setVisibility(View.GONE);
            }
        });

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                AVQuery<AVObject> avQuery = new AVQuery<>("VideoNews");
                avQuery.orderByAscending("updatedAt").whereEqualTo("enable", true);
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

                        videoNewsAdapter.notifyDataSetChanged();
                        xRecyclerView.refreshComplete();
                        showRefreshTip(list.size(), "视频");
                    }
                });
            }

            @Override
            public void onLoadMore() {

            }
        });

        return rootView;
    }

    private void showRefreshTip(int msgCount, String msgType) {
        tvRefreshResultTip.setText(getString(R.string.x_recycler_view_refresh_tip, String.valueOf(msgCount), msgType));
        showRefreshTipAnimator();
    }

    private void showRefreshTip(String msg) {
        tvRefreshResultTip.setText(msg);
        showRefreshTipAnimator();
    }

    private void showRefreshTipAnimator() {
        int deltaY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, Resources.getSystem()
                .getDisplayMetrics());

        if (hideRefreshTipAnimator == null) {
            hideRefreshTipAnimator = ObjectAnimator.ofFloat(tvRefreshResultTip, "translationY", 0, -deltaY)
                    .setDuration(200);
        }

        if (!isRefreshTipShow) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(tvRefreshResultTip, "translationY", -deltaY, 0)
                    .setDuration(200);
            if (isGoing2HideRefreshTip) {
                // 告诉 即将到来的 Runnable 不要执行Run方法体
                isGoing2HideRefreshTip = false;
            }
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isGoing2HideRefreshTip = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideRefreshTipAnimator.start();
                        }
                    }, 2000);
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
