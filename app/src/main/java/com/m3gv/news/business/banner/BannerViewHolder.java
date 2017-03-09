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
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.view.BannerIndicator;
import com.m3gv.news.common.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by meikai on 17/3/5.
 */
public class BannerViewHolder extends RecyclerView.ViewHolder {

    private AutoScrollViewPager bannerViewPager;
    private BannerIndicator bannerIndicator;

    public BannerViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_root, parent, false));

        bannerViewPager = (AutoScrollViewPager) itemView.findViewById(R.id.banner_view_pager);
        bannerViewPager.setCycle(true);
        bannerViewPager.startAutoScroll(2000);

        bannerIndicator = (BannerIndicator) itemView.findViewById(R.id.banner_indicator);
    }


    public void onBindViewHolder(final BannerEntity bannerEntity) {

        if (CollectionUtil.isEmpty(bannerEntity.newsId)) {
            bannerViewPager.setVisibility(View.GONE);
        }

        bannerViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return bannerEntity.newsId.size() > 1 ? Short.MAX_VALUE : 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {

                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                position = position % bannerEntity.newsId.size();

                ImageView imageView = new ImageView(container.getContext());
                Glide.with(container.getContext()).load(bannerEntity.thumbnailUrl.get(position)).centerCrop().into(
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
            }
        });

        int size = bannerEntity.newsId.size();
        bannerViewPager.setCurrentItem(Short.MAX_VALUE / 2 / size * size);

        bannerIndicator.init(bannerEntity, 0);
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bannerIndicator.setIndicatorPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}
