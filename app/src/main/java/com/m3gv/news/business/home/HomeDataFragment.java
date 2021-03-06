package com.m3gv.news.business.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.business.gamedata.HeroListFragment;
import com.m3gv.news.common.util.DimenUtil;
import com.m3gv.news.common.view.magicindicator.MagicIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 16/12/26.
 */
public class HomeDataFragment extends M3gBaseFragment {

    private MagicIndicator magicIndicator;
    private CommonNavigator commonNavigator;
    private ViewPager articleViewPager;

    private List<CategoryEntity> categoryList = new ArrayList<>();

    public static HomeDataFragment newInstance() {
        Bundle args = new Bundle();

        HomeDataFragment fragment = new HomeDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return rootView = inflater.inflate(R.layout.home_data_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryList = new ArrayList<>();
        categoryList.add(new CategoryEntity("英雄", "HeroInfo"));
//        categoryList.add(new CategoryEntity("物品", "HeroInfo"));
//        categoryList.add(new CategoryEntity("野怪", "HeroInfo"));
//        categoryList.add(new CategoryEntity("许愿", "HeroInfo"));

        magicIndicator = f(R.id.magic_indicator);
        articleViewPager = f(R.id.article_view_pager);
        articleViewPager.setOffscreenPageLimit(categoryList.size());
        articleViewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return HeroListFragment.newInstance();
            }

            @Override
            public int getCount() {
                return categoryList.size();
            }
        });

        commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(false);
        commonNavigator.setScrollPivotX(0.65f);

        magicIndicator.setViewPager(articleViewPager);
        magicIndicator.setNavigator(commonNavigator);

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return categoryList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(categoryList.get(index).categoryName);
                simplePagerTitleView.setNormalColor(0xaaffffff);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setSelectedTextSize(16);
                simplePagerTitleView.setNormalTextSize(14);
                simplePagerTitleView.setGravity(Gravity.CENTER_VERTICAL);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        articleViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(DimenUtil.dp2px(context, 2));
                indicator.setLineWidth(DimenUtil.dp2px(context, 30));
                indicator.setRoundRadius(DimenUtil.dp2px(context, 1));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                List<String> colorList = new ArrayList<>();
                colorList.add("#ffffff");
                indicator.setColorList(colorList);
                return indicator;
            }
        });
    }
}
