package com.m3gv.news.business.gamedata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.m3gv.news.business.NewsListFragment;
import com.m3gv.news.common.db.RealmDbHelper;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

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

        new GetHeroListAsyncTask(this, false).execute(dataList);

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new GetHeroListAsyncTask(HeroListFragment.this, true).execute(dataList);
            }

            @Override
            public void onLoadMore() {

            }
        });

        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void onGetDataSuccess(List<HeroEntity> heroEntities, boolean isPullRefresh) {
        if (isPullRefresh) {
            xRecyclerView.refreshComplete();
        }

        if (CollectionUtil.isEmpty(heroEntities)) {
            showRefreshTip("没有发现新英雄");
            return;
        }

        dataList.addAll(0, heroEntities);
        heroAdapter.notifyItemRangeInserted(1, heroEntities.size());
        xRecyclerView.setVisibility(View.VISIBLE);
        loadingViewGroup.setVisibility(View.GONE);
    }

    private static class GetHeroListAsyncTask extends AsyncTask<Object, Integer, List<HeroEntity>> {

        private WeakReference<HeroListFragment> heroListFragmentWeakReference;
        private boolean isPullRefresh;

        public GetHeroListAsyncTask(HeroListFragment heroListFragment, boolean isPullRefresh) {
            this.heroListFragmentWeakReference = new WeakReference<>(heroListFragment);
            this.isPullRefresh = isPullRefresh;
        }

        @Override
        protected List<HeroEntity> doInBackground(Object[] params) {
            List<HeroEntity> inShowDataList = (List<HeroEntity>) params[0];

            List<HeroEntity> heroEntityAbstractList = new ArrayList<>();

            //第一步：查询本地数据库是否缓存的数据
            RealmQuery<HeroEntity> realmQuery = Realm.getDefaultInstance().where(HeroEntity.class);
            if (CollectionUtil.isNotEmpty(inShowDataList)) {
                realmQuery.greaterThan("heroId", inShowDataList.get(0).heroId);
            }
            RealmResults<HeroEntity> realmQueryAll = realmQuery.findAllSorted("heroId", Sort.DESCENDING);
            List<HeroEntity> dbHeroEntityList = Realm.getDefaultInstance().copyFromRealm(realmQueryAll);

            if (CollectionUtil.isEmpty(dbHeroEntityList)) {
                //第二步：如果本地数据库没有缓存，则从服务器请求，并保存到本地数据库
                AVQuery<AVObject> avQuery = new AVQuery<>("HeroData");
                avQuery.orderByAscending("heroId");
                if (CollectionUtil.isNotEmpty(inShowDataList)) {
                    avQuery.whereGreaterThan("heroId", inShowDataList.get(0).heroId);
                }
                avQuery.whereEqualTo("enable", true);
                avQuery.limit(PAGE_LIMIT);
                List<AVObject> list = null;
                try {
                    list = avQuery.find();
                } catch (AVException e) {
                    e.printStackTrace();
                }

                if (CollectionUtil.isNotEmpty(list)) {
                    Collections.reverse(list); // 反转，保证 heroId 最小的显示在最下面
                    for (int i = 0; i < list.size(); i++) {
                        heroEntityAbstractList.add(HeroEntity.parse((list.get(i))));
                    }
                }

                //保存到本地数据库
                RealmDbHelper.getInstance().saveList(heroEntityAbstractList);

            } else {
                //第三步：如果本地数据库有缓存，则将缓存的realm格式的数据转为内存数据
                heroEntityAbstractList.addAll(dbHeroEntityList);
            }

            // 第四步：每隔 N 个位置插入一条广告item标记
//            int N = 4;
//            if (CollectionUtil.isNotEmpty(heroEntityAbstractList)) {
//                int groupCount = heroEntityAbstractList.size() / N;
//                for (int i = 0; i < groupCount; i++) {
//                    HeroEntity adHeroEntity = new HeroEntity();
//                    adHeroEntity.itemType = ItemType.YOUMI_AD_DATA;
//                    heroEntityAbstractList.add((i + 1) * N + i, adHeroEntity);
//                }
//            }

            return heroEntityAbstractList;
        }

        @Override
        protected void onPostExecute(List<HeroEntity> heroEntities) {
            super.onPostExecute(heroEntities);
            HeroListFragment heroListFragment = heroListFragmentWeakReference.get();
            if (heroListFragment != null && !heroListFragment.hasDestroyed()) {
                heroListFragment.onGetDataSuccess(heroEntities, isPullRefresh);
            }
        }
    }
}
