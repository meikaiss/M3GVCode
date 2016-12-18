package com.m3gv.news.business.article;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.m3gv.news.R;
import com.m3gv.news.business.NewsListFragment;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 16/12/18.
 */
public class ArticleListFragment extends NewsListFragment {

    private List<ArticleNewsEntity> dataList = new ArrayList<>();
    private ArticleNewsAdapter articleNewsAdapter;

    public static ArticleListFragment newInstance() {
        Bundle args = new Bundle();

        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        articleNewsAdapter = new ArticleNewsAdapter(getActivity(), dataList, xRecyclerView.isPullRefreshEnabled());
        xRecyclerView.setAdapter(articleNewsAdapter);

        AVQuery<AVObject> avQuery = new AVQuery<>("ArticleNews");
        avQuery.limit(1);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (ArticleListFragment.this == null
                        || ArticleListFragment.this.isDestroyed()
                        || list == null) {
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    dataList.add(ArticleNewsEntity.parse((list.get(i))));
                }

                articleNewsAdapter.notifyItemInserted(dataList.size());
                xRecyclerView.setVisibility(View.VISIBLE);
                loadingViewGroup.setVisibility(View.GONE);
            }
        });

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AVQuery<AVObject> avQuery = new AVQuery<>("ArticleNews");
                        avQuery.orderByAscending("videoId").whereEqualTo("enable", true);
                        if (CollectionUtil.isNotEmpty(dataList)) {
                            avQuery.whereGreaterThan("videoId", dataList.get(0).articleId);
                        }
                        avQuery.limit(1);
                        avQuery.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (ArticleListFragment.this == null
                                        || ArticleListFragment.this.isDestroyed()
                                        || CollectionUtil.isEmpty(list)) {
                                    showRefreshTip("没有发现新资讯");
                                    xRecyclerView.refreshComplete();
                                    return;
                                }

                                for (int i = list.size() - 1; i >= 0; i--) {
                                    dataList.add(0, ArticleNewsEntity.parse(list.get(i)));
                                }

                                articleNewsAdapter.notifyItemInserted(1);
                                xRecyclerView.refreshComplete();
                                showRefreshTip(
                                        getString(R.string.x_recycler_view_refresh_tip, String.valueOf(list.size()), "视频"));
                            }
                        });
                    }
                }, 500);

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


}