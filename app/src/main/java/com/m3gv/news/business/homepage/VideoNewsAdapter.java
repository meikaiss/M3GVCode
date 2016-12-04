package com.m3gv.news.business.homepage;

import android.app.Activity;
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
import com.m3gv.news.business.video.VideoDetailActivity;

import java.util.List;

/**
 * Created by meikai on 16/12/3.
 */
public class VideoNewsAdapter extends RecyclerView.Adapter<VideoNewsAdapter.VideoNewsViewHolder> {

    private Activity activity;
    private List<VideoNewsEntity> dataList;

    public VideoNewsAdapter(Activity activity, List<VideoNewsEntity> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public VideoNewsAdapter.VideoNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_news_list_item, parent, false);

        return new VideoNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoNewsAdapter.VideoNewsViewHolder holder, int pos) {
        holder.tvTitle.setText(dataList.get(pos).videoTitle);
        holder.tvPlayCount.setText("" + dataList.get(pos).playCount);
        holder.tvZanCount.setText("" + dataList.get(pos).zanCount);
        holder.tvCaiCount.setText("" + dataList.get(pos).caiCount);

        Glide.with(activity).load(dataList.get(pos).thumbnail).centerCrop().into(new GlideDrawableImageViewTarget
                (holder.imgThumbs) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                if (dataList.get(pos).videoResolution == 4) {
                    holder.imgLabel.setImageResource(R.drawable.video_label_hot);
                } else {
                    holder.imgLabel.setImageResource(0);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VideoDetailActivity.start(activity, dataList.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class VideoNewsViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageView imgThumbs;
        public TextView tvPlayCount;
        public TextView tvZanCount;
        public TextView tvCaiCount;
        public ImageView imgLabel;

        public VideoNewsViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_video_news_list_title);
            imgThumbs = (ImageView) itemView.findViewById(R.id.img_video_news_list_thumbs);
            tvPlayCount = (TextView) itemView.findViewById(R.id.tv_play_count);
            tvZanCount = (TextView) itemView.findViewById(R.id.tv_zan_count);
            tvCaiCount = (TextView) itemView.findViewById(R.id.tv_cai_count);
            imgLabel = (ImageView) itemView.findViewById(R.id.img_label);
        }
    }
}
