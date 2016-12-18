package com.m3gv.news.business;

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

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.common.view.magicindicator.MagicIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 16/12/18.
 */
public class NewsListFragment extends M3gBaseFragment {

    protected ViewGroup emptyViewGroup;
    protected ViewGroup loadingViewGroup;
    protected ViewGroup noNetViewGroup;
    protected MagicIndicator magicIndicator;
    protected CommonNavigator commonNavigator;
    protected XRecyclerView xRecyclerView;
    protected TextView tvRefreshResultTip;

    protected boolean isRefreshTipShow = false;
    protected List<RefreshTipRunnable> refreshTipRunnableList = new ArrayList<>();

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

        return rootView;
    }


    protected void showRefreshTip(String msg) {
        tvRefreshResultTip.setText(msg);
        showRefreshTipAnimator();
    }

    protected void showRefreshTipAnimator() {
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
