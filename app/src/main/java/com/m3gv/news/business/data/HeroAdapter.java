package com.m3gv.news.business.data;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.m3gv.news.R;

import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */
public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {

    private Activity activity;
    private List<HeroEntity> dataList;
    private boolean isPullRefreshEnable;

    public HeroAdapter(Activity activity, List<HeroEntity> dataList, boolean isPullRefreshEnable) {
        this.activity = activity;
        this.dataList = dataList;
        this.isPullRefreshEnable = isPullRefreshEnable;
    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hero_list_item, parent, false);

        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HeroViewHolder holder, final int pos) {

        holder.imgThumbs.setBackgroundColor(Color.GRAY);
        holder.imgLabel.setImageResource(0);

        holder.tvHeroName.setText(dataList.get(pos).heroName);
        holder.tvHeroDesc.setText(dataList.get(pos).heroDesc);

        Glide.with(activity).load(dataList.get(pos).heroIcon).centerCrop().into(new GlideDrawableImageViewTarget
                (holder.imgThumbs) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArticleNewsDetailActivity.start(activity,
//                        dataList.get(holder.getAdapterPosition() - (isPullRefreshEnable ? 1 : 0)));
                //是因为头部下拉刷新占了一个位置
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class HeroViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeroName;
        public ImageView imgThumbs;
        public TextView tvHeroDesc;
        public ImageView imgLabel;

        public HeroViewHolder(View itemView) {
            super(itemView);
            tvHeroName = (TextView) itemView.findViewById(R.id.tv_hero_name_list_title);
            imgThumbs = (ImageView) itemView.findViewById(R.id.img_hero_list_thumbs);
            tvHeroDesc = (TextView) itemView.findViewById(R.id.tv_hero_desc_list_title);
            imgLabel = (ImageView) itemView.findViewById(R.id.img_label);
        }
    }
}