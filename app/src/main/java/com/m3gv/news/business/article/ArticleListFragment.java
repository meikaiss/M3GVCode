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
import com.m3gv.news.base.M3bBaseEntity;
import com.m3gv.news.business.NewsListFragment;
import com.m3gv.news.business.banner.BannerEntity;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by meikai on 16/12/18.
 */
public class ArticleListFragment extends NewsListFragment {

    private static final String KEY_CATEGORY_ID = "key_category_id";

    private String tableName;

    private List<M3bBaseEntity> dataList = new ArrayList<>();
    private ArticleNewsAdapter articleNewsAdapter;

    public static ArticleListFragment newInstance(String tableName) {
        Bundle args = new Bundle();
        args.putString(KEY_CATEGORY_ID, tableName);
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        tableName = getArguments().getString(KEY_CATEGORY_ID);

        articleNewsAdapter = new ArticleNewsAdapter(getActivity(), dataList, xRecyclerView.isPullRefreshEnabled());
        xRecyclerView.setAdapter(articleNewsAdapter);

        loadNewsList();

        return rootView;
    }

    private void loadBanner() {
        AVQuery<AVObject> avQuery = new AVQuery<>("Banner");
        avQuery.whereEqualTo("ChannelName", tableName).whereEqualTo("enable", true);

        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (ArticleListFragment.this == null
                        || ArticleListFragment.this.hasDestroyed()
                        || CollectionUtil.isEmpty(list)) {
                    return;
                }

                Collections.reverse(list);

                for (int i = 0; i < list.size(); i++) {
                    dataList.add(0, BannerEntity.parse(list.get(i)));
                }

                articleNewsAdapter.notifyItemRangeInserted(1, list.size());
            }
        });
    }

    private void loadNewsList() {
        AVQuery<AVObject> avQuery = new AVQuery<>(tableName);
        avQuery.orderByAscending("articleId").whereEqualTo("enable", true);
        avQuery.limit(PAGE_LIMIT);

        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (ArticleListFragment.this == null
                        || ArticleListFragment.this.hasDestroyed()
                        || list == null) {
                    showRefreshTip("没有发现新资讯");
                    return;
                }

                Collections.reverse(list);

                for (int i = 0; i < list.size(); i++) {
                    dataList.add(ArticleNewsEntity.parse((list.get(i))));
                }

                articleNewsAdapter.notifyItemRangeInserted(1, list.size());
                xRecyclerView.setVisibility(View.VISIBLE);
                loadingViewGroup.setVisibility(View.GONE);

                loadBanner();
            }
        });

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AVQuery<AVObject> avQuery = new AVQuery<>(tableName);
                        avQuery.orderByAscending("articleId");
                        if (CollectionUtil.isNotEmpty(dataList)) {
                            if (dataList.get(0) instanceof ArticleNewsEntity) {
                                avQuery.whereGreaterThan("articleId",
                                        ((ArticleNewsEntity) (dataList.get(0))).articleId);
                            } else if (dataList.get(1) instanceof ArticleNewsEntity) {
                                avQuery.whereGreaterThan("articleId",
                                        ((ArticleNewsEntity) (dataList.get(1))).articleId);
                            }
                        }
                        avQuery.whereEqualTo("enable", true);
                        avQuery.limit(PAGE_LIMIT);
                        avQuery.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (ArticleListFragment.this == null
                                        || ArticleListFragment.this.hasDestroyed()
                                        || CollectionUtil.isEmpty(list)) {
                                    showRefreshTip("没有发现新资讯");
                                    xRecyclerView.refreshComplete();
                                    return;
                                }

                                boolean hasBanner = false;
                                if (CollectionUtil.isNotEmpty(dataList) && dataList.get(0).entityType ==
                                        M3bBaseEntity.EntityType.Banner) {
                                    hasBanner = true;
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    dataList.add(hasBanner ? 1 : 0, ArticleNewsEntity.parse(list.get(i)));
                                }

                                articleNewsAdapter.notifyItemRangeInserted(1 + (hasBanner ? 1 : 0), list.size());
                                xRecyclerView.refreshComplete();
                                showRefreshTip(getString(R.string.x_recycler_view_refresh_tip,
                                        String.valueOf(list.size()), "资讯"));

                            }
                        });
                    }
                }, 200);
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

}