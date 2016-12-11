package com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.titles;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.m3gv.news.common.util.DensityUtil;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;


/**
 * Created by meikai on 16/10/9.
 */

public class PagerTitleContainerView extends FrameLayout implements IMeasurablePagerTitleView {

    private int mSelectedColor;
    private int mNormalColor;

    private TextView titleTv;

    public PagerTitleContainerView(Context context) {
        super(context);

        titleTv = new TextView(context);
        int paddingHorizontal = DensityUtil.dp2px(context, 8);
        int paddingVertical = DensityUtil.dp2px(context, 3);
        titleTv.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        titleTv.setSingleLine();
        titleTv.setEllipsize(TextUtils.TruncateAt.END);

        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;

        addView(titleTv, lp);
    }

    public void setText(String title) {
        titleTv.setText(title);
    }

    public void setTitleBackgroundResource(@DrawableRes int backgroundResourceId) {
        titleTv.setBackgroundResource(backgroundResourceId);
    }

    @Override
    public int getContentLeft() {
        Rect bound = new Rect();
        titleTv.getPaint().getTextBounds(titleTv.getText().toString(), 0, titleTv.getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = titleTv.getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        Rect bound = new Rect();
        titleTv.getPaint().getTextBounds(titleTv.getText().toString(), 0, titleTv.getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = titleTv.getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        titleTv.setTextColor(mSelectedColor);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        titleTv.setTextColor(mNormalColor);
    }



    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }



    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
    }

}
