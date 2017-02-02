package com.m3gv.news.business.gamedata;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.m3gv.news.R;
import com.m3gv.news.business.youmiAd.ItemType;
import com.m3gv.news.common.util.CollectionUtil;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */
public class HeroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<HeroEntity> dataList;
    private boolean isPullRefreshEnable;

    public HeroAdapter(Activity activity, List<HeroEntity> dataList, boolean isPullRefreshEnable) {
        this.activity = activity;
        this.dataList = dataList;
        this.isPullRefreshEnable = isPullRefreshEnable;
    }

    @Override
    public int getItemViewType(int position) {
        if (CollectionUtil.isNotEmpty(dataList)) {
            return dataList.get(position).itemType.ordinal();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.REAL_DATA.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hero_list_item, parent, false);
            return new HeroViewHolder(view);
        } else if (viewType == ItemType.YOUMI_AD_DATA.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youni_ad_container, parent, false);
            return new YouMiAdViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int pos) {

        if (holder instanceof HeroViewHolder) {
            final HeroViewHolder heroViewHolder = (HeroViewHolder) holder;
            heroViewHolder.imgThumbs.setBackgroundColor(Color.GRAY);
            heroViewHolder.imgLabel.setImageResource(0);

            heroViewHolder.tvHeroName.setText(dataList.get(pos).heroName);
            heroViewHolder.tvHeroCountry.setText(dataList.get(pos).UNationType);
            heroViewHolder.tvHeroPlayType1.setText(dataList.get(pos).Type2);
            heroViewHolder.tvHeroPlayType2.setText(dataList.get(pos).Type1);
            heroViewHolder.tvHeroPlayType3.setText(dataList.get(pos).Fposition);
            heroViewHolder.rbDifficulty.setRating(dataList.get(pos).Difficulty / 2f);
            heroViewHolder.pbSurvival.setProgress(dataList.get(pos).Survial);
            heroViewHolder.pbPhysics.setProgress(dataList.get(pos).Physics);
            heroViewHolder.pbSkill.setProgress(dataList.get(pos).Technology);

            Glide.with(activity).load(dataList.get(pos).heroIcon).centerCrop().into(new GlideDrawableImageViewTarget
                    (heroViewHolder.imgThumbs) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HeroDetailActivity
                            .start(activity,
                                    dataList.get(heroViewHolder.getAdapterPosition() - (isPullRefreshEnable ? 1 : 0)));
                    //是因为头部下拉刷新占了一个位置
                }
            });
        } else if (holder instanceof YouMiAdViewHolder) {
            YouMiAdViewHolder youMiAdViewHolder = (YouMiAdViewHolder) holder;
            if (youMiAdViewHolder.hasShowAd) {

            } else {
                final ViewGroup adContainer = (ViewGroup) holder.itemView;
                adContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                final View adView = BannerManager.getInstance(adContainer.getContext())
                        .getBannerView(adContainer.getContext(), new BannerViewListener() {
                            @Override
                            public void onRequestSuccess() {
                                View childAdView = adContainer.getChildAt(1);
                                adContainer.getLayoutParams().height = childAdView.getMeasuredHeight();
                                childAdView.requestLayout();
                            }

                            @Override
                            public void onSwitchBanner() {

                            }

                            @Override
                            public void onRequestFailed() {
                                ((TextView) adContainer.getChildAt(0)).setText("哎呀，如果广告没有显示出来，开发哥哥就没有精力开发更好用的产品");
                                adContainer.getChildAt(0).setVisibility(View.VISIBLE);
                            }
                        });
                if (adView != null) {
                    adContainer.addView(adView);
                }
                youMiAdViewHolder.hasShowAd = true;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    public class YouMiAdViewHolder extends RecyclerView.ViewHolder {

        public boolean hasShowAd;

        public YouMiAdViewHolder(View itemView) {
            super(itemView);
        }

    }

    public class HeroViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeroName;
        public ImageView imgThumbs;
        public TextView tvHeroCountry;
        public TextView tvHeroPlayType1;
        public TextView tvHeroPlayType2;
        public TextView tvHeroPlayType3;
        public ImageView imgLabel;
        public RatingBar rbDifficulty;
        public ProgressBar pbSurvival;
        public ProgressBar pbPhysics;
        public ProgressBar pbSkill;


        public HeroViewHolder(View itemView) {
            super(itemView);
            tvHeroName = (TextView) itemView.findViewById(R.id.tv_hero_name);
            tvHeroCountry = (TextView) itemView.findViewById(R.id.tv_hero_county);
            tvHeroPlayType1 = (TextView) itemView.findViewById(R.id.tv_hero_play_type_1);
            tvHeroPlayType2 = (TextView) itemView.findViewById(R.id.tv_hero_play_type_2);
            tvHeroPlayType3 = (TextView) itemView.findViewById(R.id.tv_hero_play_type_3);
            imgThumbs = (ImageView) itemView.findViewById(R.id.img_hero_list_thumbs);
            imgLabel = (ImageView) itemView.findViewById(R.id.img_label);
            rbDifficulty = (RatingBar) itemView.findViewById(R.id.rating_bar_difficulty);
            pbSurvival = (ProgressBar) itemView.findViewById(R.id.progress_bar_survival);
            pbPhysics = (ProgressBar) itemView.findViewById(R.id.progress_bar_physics);
            pbSkill = (ProgressBar) itemView.findViewById(R.id.progress_bar_skill);
        }
    }
}
