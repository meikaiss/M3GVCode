package com.m3gv.news.business.data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
}
