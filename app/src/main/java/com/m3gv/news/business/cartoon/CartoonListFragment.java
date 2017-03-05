package com.m3gv.news.business.cartoon;

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
 * Created by meikai on 16/12/18.
 */
public class CartoonListFragment extends NewsListFragment {

    private static final String KEY_CATEGORY_ID = "key_category_id";

    private String tableName;

    private List<CartoonEntity> dataList = new ArrayList<>();
    private CartoonNewsAdapter cartoonNewsAdapter;

    public static CartoonListFragment newInstance(String tableName) {
        Bundle args = new Bundle();
        args.putString(KEY_CATEGORY_ID, tableName);
        CartoonListFragment fragment = new CartoonListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        tableName = getArguments().getString(KEY_CATEGORY_ID);

        cartoonNewsAdapter = new CartoonNewsAdapter(getActivity(), dataList, xRecyclerView.isPullRefreshEnabled());
        xRecyclerView.setAdapter(cartoonNewsAdapter);

        AVQuery<AVObject> avQuery = new AVQuery<>(tableName);
        avQuery.orderByAscending("cartoonId").whereEqualTo("enable", true);
        avQuery.limit(PAGE_LIMIT);

        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (CartoonListFragment.this == null
                        || CartoonListFragment.this.hasDestroyed()
                        || list == null) {
                    showRefreshTip("没有发现新漫画");
                    return;
                }

                Collections.reverse(list);

                for (int i = 0; i < list.size(); i++) {
                    dataList.add(CartoonEntity.parse((list.get(i))));
                }

                cartoonNewsAdapter.notifyItemRangeInserted(1, list.size());
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
                        AVQuery<AVObject> avQuery = new AVQuery<>(tableName);
                        avQuery.orderByAscending("cartoonId");
                        if (CollectionUtil.isNotEmpty(dataList)) {
                            avQuery.whereGreaterThan("cartoonId", dataList.get(0).cartoonId);
                        }
                        avQuery.whereEqualTo("enable", true);
                        avQuery.limit(PAGE_LIMIT);
                        avQuery.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (CartoonListFragment.this == null
                                        || CartoonListFragment.this.hasDestroyed()
                                        || CollectionUtil.isEmpty(list)) {
                                    showRefreshTip("没有发现新漫画");
                                    xRecyclerView.refreshComplete();
                                    return;
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    dataList.add(0, CartoonEntity.parse(list.get(i)));
                                }

                                cartoonNewsAdapter.notifyItemRangeInserted(1, list.size());
                                xRecyclerView.refreshComplete();
                                showRefreshTip(
                                        getString(R.string.x_recycler_view_refresh_tip, String.valueOf(list.size()),
                                                "漫画"));
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