package com.m3gv.news.business.article;

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
import com.m3gv.news.base.M3bBaseEntity;
import com.m3gv.news.business.banner.BannerEntity;
import com.m3gv.news.business.banner.BannerViewHolder;
import com.m3gv.news.common.util.StringUtil;
import com.m3gv.news.common.util.UnitUtil;

import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */
public class ArticleNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<M3bBaseEntity> dataList;
    private boolean isPullRefreshEnable;

    public ArticleNewsAdapter(Activity activity, List<M3bBaseEntity> dataList, boolean isPullRefreshEnable) {
        this.activity = activity;
        this.dataList = dataList;
        this.isPullRefreshEnable = isPullRefreshEnable;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).entityType.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == M3bBaseEntity.EntityType.Normal.ordinal()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_news_list_item, parent, false);

            return new ArticleNewsViewHolder(view);
        } else {

            return new BannerViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int pos) {
        if (holder instanceof ArticleNewsViewHolder) {
            ArticleNewsViewHolder articleNewsViewHolder = (ArticleNewsViewHolder) holder;

            ArticleNewsEntity articleNewsEntity = (ArticleNewsEntity) dataList.get(pos);

            articleNewsViewHolder.imgThumbs.setBackgroundColor(Color.GRAY);
            articleNewsViewHolder.imgLabel.setImageResource(0);

            articleNewsViewHolder.tvTitle.setText(articleNewsEntity.articleTitle);
            articleNewsViewHolder.tvSource.setText(articleNewsEntity.source);
            articleNewsViewHolder.tvPlayCount.setText(UnitUtil.toWan(articleNewsEntity.readCount));

            if (StringUtil.isNotEmpty(articleNewsEntity.thumbnail)) {
                Glide.with(activity).load(articleNewsEntity.thumbnail).centerCrop()
                        .into(new GlideDrawableImageViewTarget
                                (articleNewsViewHolder.imgThumbs) {
                            @Override
                            public void onResourceReady(GlideDrawable resource,
                                    GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                            }
                        });
            } else {
                articleNewsViewHolder.imgThumbs.setImageResource(R.drawable.default_image);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArticleNewsDetailActivity.start(activity,
                            (ArticleNewsEntity) dataList
                                    .get(holder.getAdapterPosition() - (isPullRefreshEnable ? 1 : 0)));
                    //是因为头部下拉刷新占了一个位置
                }
            });
        } else if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).onBindViewHolder((BannerEntity) dataList.get(pos));
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ArticleNewsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageView imgThumbs;
        public TextView tvSource;
        public TextView tvPlayCount;
        public ImageView imgLabel;

        public ArticleNewsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_article_news_list_title);
            imgThumbs = (ImageView) itemView.findViewById(R.id.img_article_news_list_thumbs);
            tvSource = (TextView) itemView.findViewById(R.id.tv_source);
            tvPlayCount = (TextView) itemView.findViewById(R.id.tv_play_count);
            imgLabel = (ImageView) itemView.findViewById(R.id.img_label);
        }
    }
}
