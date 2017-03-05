package com.m3gv.news.business.banner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.m3gv.news.R;

/**
 * Created by meikai on 17/3/5.
 */

public class BannerViewHolder extends RecyclerView.ViewHolder {

    private ViewPager bannerViewPager;

    public BannerViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_root, parent, false));

        bannerViewPager = (ViewPager) itemView.findViewById(R.id.banner_view_pager);
    }


    public void onBindViewHolder(final BannerEntity bannerEntity) {

        bannerViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {

                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(container.getContext());
                Glide.with(container.getContext()).load(bannerEntity.thumbnailUrl).centerCrop().into(
                        new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onResourceReady(GlideDrawable resource,
                                    GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                            }
                        });
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeAllViews();
            }
        });
    }

}
