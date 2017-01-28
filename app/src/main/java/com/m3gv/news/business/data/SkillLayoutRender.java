package com.m3gv.news.business.data;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.m3gv.news.R;
import com.m3gv.news.common.util.DensityUtil;
import com.m3gv.news.common.view.magicindicator.MagicIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by meikai on 17/1/24.
 */
public class SkillLayoutRender {

    private HeroEntity heroEntity;

    private LinearLayout rootLinearLayout;

    private MagicIndicator skillMagicIndicator;
    private ViewPager skillViewPager;

    private CommonNavigator commonNavigator;
    private CommonNavigatorAdapter navigatorAdapter;

    public SkillLayoutRender(LinearLayout linearLayout, HeroEntity heroEntity) {
        this.rootLinearLayout = linearLayout;
        this.heroEntity = heroEntity;
        initSkillView();
    }

    public void initSkillView() {
        skillMagicIndicator = (MagicIndicator) rootLinearLayout.findViewById(R.id.skill_magic_indicator);
        skillViewPager = (ViewPager) rootLinearLayout.findViewById(R.id.skill_view_pager);
        skillViewPager.setOffscreenPageLimit(4);
        skillViewPager.setAdapter(new PagerAdapter() {

            //用于存储回收掉的View
            private List<WeakReference<LinearLayout>> viewList = new ArrayList<>();

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int skillPosition) {
                LinearLayout itemView = (LinearLayout) LayoutInflater.from(container.getContext())
                        .inflate(R.layout.custom_view_skill_describe, container, false);

                TextView tvSkillName = (TextView) itemView.findViewById(R.id.tv_skill_name);
                TextView tvSkillScope = (TextView) itemView.findViewById(R.id.tv_skill_scope);
                TextView tvSkillEffect = (TextView) itemView.findViewById(R.id.tv_skill_effect);
                TextView tvSkillMagic = (TextView) itemView.findViewById(R.id.tv_skill_magic);
                TextView tvSkillCD = (TextView) itemView.findViewById(R.id.tv_skill_CD);
                TextView tvSkillLevel1Effect = (TextView) itemView.findViewById(R.id.tv_skill_level_1_effect);
                TextView tvSkillLevel2Effect = (TextView) itemView.findViewById(R.id.tv_skill_level_2_effect);
                TextView tvSkillLevel3Effect = (TextView) itemView.findViewById(R.id.tv_skill_level_3_effect);
                TextView tvSkillLevel4Effect = (TextView) itemView.findViewById(R.id.tv_skill_level_4_effect);

                tvSkillName.setText(heroEntity.skillName[skillPosition]);
                tvSkillScope.setText(heroEntity.skillScope[skillPosition]);
                tvSkillEffect.setText(heroEntity.skillEffect[skillPosition]);
                tvSkillMagic.setText(heroEntity.skillMagic[skillPosition]);
                tvSkillCD.setText(heroEntity.skillCD[skillPosition]);
                tvSkillLevel1Effect.setText(heroEntity.skillLevel1Effect[skillPosition]);
                tvSkillLevel2Effect.setText(heroEntity.skillLevel2Effect[skillPosition]);
                tvSkillLevel3Effect.setText(heroEntity.skillLevel3Effect[skillPosition]);
                tvSkillLevel4Effect.setText(heroEntity.skillLevel4Effect[skillPosition]);

                container.addView(itemView);
                viewList.add(new WeakReference<>(itemView));

                return itemView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                LinearLayout itemView = viewList.get(position).get();
                if (itemView != null) {
                    container.removeView(itemView);
                }
            }
        });

        commonNavigator = new CommonNavigator(rootLinearLayout.getContext());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setScrollPivotX(0.85f);

        skillMagicIndicator.setViewPager(skillViewPager);
        skillMagicIndicator.setNavigator(commonNavigator);
        navigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final SkillPagerTitleView skillPagerTitleView = new SkillPagerTitleView(context);

                Glide.with(context).load(heroEntity.skillIcon[index]).into(new GlideDrawableImageViewTarget
                        (skillPagerTitleView.getSkillImageView()) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                            GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                    }
                });

                skillPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skillViewPager.setCurrentItem(index);
                    }
                });

                return skillPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(DensityUtil.dp2px(context, 2));
                indicator.setLineWidth(DensityUtil.dp2px(context, 50));
                indicator.setRoundRadius(DensityUtil.dp2px(context, 1));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.0f));
                List<String> colorList = new ArrayList<>();
                colorList.add("#ff3f3e");
                indicator.setColorList(colorList);
                return indicator;
            }
        };
        commonNavigator.setAdapter(navigatorAdapter);
    }
}
