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
import com.m3gv.news.common.util.StringUtil;
import com.m3gv.news.common.util.UnitUtil;

import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */
public class ArticleNewsAdapter extends RecyclerView.Adapter<ArticleNewsAdapter.ArticleNewsViewHolder> {

    private Activity activity;
    private List<ArticleNewsEntity> dataList;
    private boolean isPullRefreshEnable;

    public ArticleNewsAdapter(Activity activity, List<ArticleNewsEntity> dataList, boolean isPullRefreshEnable) {
        this.activity = activity;
        this.dataList = dataList;
        this.isPullRefreshEnable = isPullRefreshEnable;
    }

    @Override
    public ArticleNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_news_list_item, parent, false);

        return new ArticleNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleNewsViewHolder holder, final int pos) {

        holder.imgThumbs.setBackgroundColor(Color.GRAY);
        holder.imgLabel.setImageResource(0);

        holder.tvTitle.setText(dataList.get(pos).articleTitle);
        holder.tvSource.setText(dataList.get(pos).source);
        holder.tvPlayCount.setText(UnitUtil.toWan(dataList.get(pos).readCount));

        if (StringUtil.isNotEmpty(dataList.get(pos).thumbnail)) {
            Glide.with(activity).load(dataList.get(pos).thumbnail).centerCrop().into(new GlideDrawableImageViewTarget
                    (holder.imgThumbs) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                }
            });
        } else {
            holder.imgThumbs.setImageResource(R.drawable.default_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleNewsDetailActivity.start(activity,
                        dataList.get(holder.getAdapterPosition() - (isPullRefreshEnable ? 1 : 0)));
                //是因为头部下拉刷新占了一个位置
            }
        });
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
