package com.m3gv.news.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.m3gv.news.R;
import com.m3gv.news.business.banner.BannerEntity;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.util.DimenUtil;


/**
 * Created by meikai on 17/3/8.
 */
public class BannerIndicator extends LinearLayout {

    public BannerIndicator(Context context) {
        super(context);
    }

    public BannerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIndicatorPosition(int newPos) {

        newPos = newPos % getChildCount();

        for (int i = 0; i < getChildCount(); i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            if (i == newPos) {
                imageView.setSelected(true);
            } else {
                imageView.setSelected(false);
            }
        }
    }

    public void init(BannerEntity bannerList, int selectedIndex) {
        if (CollectionUtil.isEmpty(bannerList.newsId)) {
            return;
        }

        removeAllViews();

        int marginLeftRight = DimenUtil.dp2px(getContext(), 3);

        for (int i = 0; i < bannerList.newsId.size(); i++) {

            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(R.drawable.banner_indicator_bg);

            LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT);
            lp.setMargins(marginLeftRight, 0, marginLeftRight, 0);
            addView(imageView, lp);

            if (i == selectedIndex) {
                imageView.setSelected(true);
            } else {
                imageView.setSelected(false);
            }
        }
    }

}
