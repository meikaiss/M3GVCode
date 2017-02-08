package com.m3gv.news.business.gamedata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.util.UIUtil;

import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

/**
 * Created by meikai on 17/1/18.
 */

public class HeroDetailActivity extends M3gBaseActivity {

    private ImageView imageViewHeroIcon;
    private TextView textViewHeroName;
    private TextView textViewCountry;
    private TextView textViewPlayType1;
    private TextView textViewPlayType2;
    private TextView textViewPlayType3;
    private TextView textViewAttr1;
    private TextView textViewAttr2;
    private TextView textViewAttr3;
    private TextView textViewAttr4;
    private TextView textViewAttr5;
    private TextView textViewAttr6;

    private RatingBar ratingBarDifficulty;
    private ProgressBar progressBarSurvial;
    private ProgressBar progressBarPhysics;
    private ProgressBar progressBarSkill;

    private TextView tvHeroDesc;

    private SkillLayoutRender skillLayoutRender;

    private HeroEntity heroEntity;

    public static void start(Activity activity, HeroEntity heroEntity) {
        Intent intent = new Intent(activity, HeroDetailActivity.class);
        intent.putExtra("heroEntity", heroEntity);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_detail_activity);

        heroEntity = getIntent().getParcelableExtra("heroEntity");

        imageViewHeroIcon = f(R.id.img_hero_list_thumbs);
        textViewHeroName = f(R.id.tv_hero_name);
        textViewCountry = f(R.id.tv_hero_county);
        textViewPlayType1 = f(R.id.tv_hero_play_type_1);
        textViewPlayType2 = f(R.id.tv_hero_play_type_2);
        textViewPlayType3 = f(R.id.tv_hero_play_type_3);
        textViewAttr1 = f(R.id.tv_attr_1);
        textViewAttr2 = f(R.id.tv_attr_2);
        textViewAttr3 = f(R.id.tv_attr_3);
        textViewAttr4 = f(R.id.tv_attr_4);
        textViewAttr5 = f(R.id.tv_attr_5);
        textViewAttr6 = f(R.id.tv_attr_6);

        ratingBarDifficulty = f(R.id.rating_bar_difficulty);
        progressBarSurvial = f(R.id.progress_bar_survial);
        progressBarPhysics = f(R.id.progress_bar_physics);
        progressBarSkill = f(R.id.progress_bar_skill);

        tvHeroDesc = f(R.id.tv_hero_detail_desc);

        init();

        f(R.id.img_btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeroDetailActivity.this.finish();
            }
        });

        if (Math.random() > 0.5) {
            setupSlideableSpotAd();
        }
    }

    private void init() {
        Glide.with(this).load(heroEntity.heroIcon).centerCrop().into(new GlideDrawableImageViewTarget
                (imageViewHeroIcon) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
            }
        });

        textViewHeroName.setText(heroEntity.heroName);
        textViewCountry.setText(heroEntity.UNationType);
        textViewPlayType1.setText(heroEntity.Type2);
        textViewPlayType2.setText(heroEntity.Type1);
        textViewPlayType3.setText(heroEntity.Fposition);
        textViewAttr1.setText(heroEntity.attack);
        textViewAttr2.setText(heroEntity.armor);
        textViewAttr3.setText(heroEntity.speed);
        textViewAttr4.setText(heroEntity.strength);
        textViewAttr5.setText(heroEntity.agility);
        textViewAttr6.setText(heroEntity.intelligence);

        ratingBarDifficulty.setRating(heroEntity.Difficulty / 2f);
        progressBarSurvial.setProgress(heroEntity.Survial);
        progressBarPhysics.setProgress(heroEntity.Physics);
        progressBarSkill.setProgress(heroEntity.Technology);

        tvHeroDesc.setText(heroEntity.heroDesc);

        skillLayoutRender = new SkillLayoutRender((LinearLayout) f(R.id.skill_root_layout), heroEntity);
    }


    @Override
    public void onBackPressed() {
        // 点击后退关闭轮播插屏广告
        if (SpotManager.getInstance(this).isSlideableSpotShowing()) {
            SpotManager.getInstance(this).hideSlideableSpot();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 轮播插屏广告
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 轮播插屏广告
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 轮播插屏广告
        SpotManager.getInstance(this).onDestroy();
    }

    /**
     * 设置轮播插屏广告
     */
    private void setupSlideableSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(this)
                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

        SpotManager.getInstance(this)
                .showSlideableSpot(this, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.e("", "轮播插屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        Log.e("", "轮播插屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                UIUtil.showToast("网络异常");
                                break;
                            case ErrorCode.NON_AD:
                                UIUtil.showToast("暂无轮播插屏广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                UIUtil.showToast("轮播插屏资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                UIUtil.showToast("请勿频繁展示");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                UIUtil.showToast("请设置插屏为可见状态");
                                break;
                            default:
                                UIUtil.showToast("请稍后再试");
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.e("", "轮播插屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        Log.e("", "轮播插屏被点击");
                        Log.e("", String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
                    }
                });
    }
}
