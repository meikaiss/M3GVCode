package com.m3gv.news.business.video;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.m3gv.news.R;
import com.m3gv.news.business.NewsListFragment;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.util.DensityUtil;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.m3gv.news.common.view.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */
public class VideoListFragment extends NewsListFragment {

    private List<VideoNewsEntity> dataList = new ArrayList<>();
    private VideoNewsAdapter videoNewsAdapter;

    private List<String> channelList = new ArrayList<>();

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
        super.onCreateView(inflater, container, savedInstanceState);

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                                        getString(R.string.x_recycler_view_refresh_tip, String.valueOf(list.size()),
                                                "视频"));
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

        magicIndicator.setNavigator(commonNavigator);

        channelList.add("每日精彩镜头");
        channelList.add("搞笑视频");

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return channelList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(channelList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#9e9e9e"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ff3f3e"));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                simplePagerTitleView.setGravity(Gravity.CENTER_VERTICAL);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("commonNavigator", "index=" + index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(DensityUtil.dp2px(context, 2));
                indicator.setLineWidth(DensityUtil.dp2px(context, 30));
                indicator.setRoundRadius(DensityUtil.dp2px(context, 1));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                List<String> colorList = new ArrayList<>();
                colorList.add("#ff3f3e");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
    }


}
