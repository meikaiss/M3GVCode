package com.m3gv.news.business.data;

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
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by meikai on 17/1/12.
 */

public class HeroListFragment extends NewsListFragment {

    private List<HeroEntity> dataList = new ArrayList<>();
    private HeroAdapter heroAdapter;

    public static HeroListFragment newInstance() {

        Bundle args = new Bundle();

        HeroListFragment fragment = new HeroListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        heroAdapter = new HeroAdapter(getActivity(), dataList, xRecyclerView.isPullRefreshEnabled());
        xRecyclerView.setAdapter(heroAdapter);

        AVQuery<AVObject> avQuery = new AVQuery<>("HeroData");
        avQuery.orderByAscending("heroId").whereEqualTo("enable", true);
        avQuery.limit(PAGE_LIMIT);

        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (HeroListFragment.this == null
                        || HeroListFragment.this.isDestroyed()
                        || list == null) {
                    showRefreshTip("没有发现新英雄");
                    return;
                }

                Collections.reverse(list);

                for (int i = 0; i < list.size(); i++) {
                    dataList.add(HeroEntity.parse((list.get(i))));
                }

                heroAdapter.notifyItemRangeInserted(1, list.size());
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
                        AVQuery<AVObject> avQuery = new AVQuery<>("HeroData");
                        avQuery.orderByAscending("heroId");
                        if (CollectionUtil.isNotEmpty(dataList)) {
                            avQuery.whereGreaterThan("heroId", dataList.get(0).heroId);
                        }
                        avQuery.whereEqualTo("enable", true);
                        avQuery.limit(PAGE_LIMIT);
                        avQuery.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (HeroListFragment.this == null
                                        || HeroListFragment.this.isDestroyed()
                                        || CollectionUtil.isEmpty(list)) {
                                    showRefreshTip("没有发现新英雄");
                                    xRecyclerView.refreshComplete();
                                    return;
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    dataList.add(0, HeroEntity.parse(list.get(i)));
                                }

                                heroAdapter.notifyItemRangeInserted(1, list.size());
                                xRecyclerView.refreshComplete();
                                showRefreshTip(
                                        getString(R.string.x_recycler_view_refresh_tip, String.valueOf(list.size()),
                                                "英雄"));
                            }
                        });
                    }
                }, 200);

            }

            @Override
            public void onLoadMore() {

            }
        });

        return rootView;
    }
}
