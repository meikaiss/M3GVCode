package com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.titles;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.m3gv.news.common.util.DensityUtil;
import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;

/**
 * 带文本的指示器标题
 */
public class SimplePagerTitleView extends AppCompatTextView implements IMeasurablePagerTitleView {
    private int mSelectedColor;
    private int mNormalColor;
    private int mSelectedTextSize;
    private int mNormalTextSize;

    public SimplePagerTitleView(Context context) {
        super(context, null);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);
        int padding = DensityUtil.dp2px(context, 10);
        setPadding(padding, 0, padding, 0);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        setTextColor(mSelectedColor);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, mSelectedTextSize);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        setTextColor(mNormalColor);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, mNormalTextSize);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    }

    @Override
    public int getContentLeft() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public void setSelectedTextSize(int mSelectedTextSize) {
        this.mSelectedTextSize = mSelectedTextSize;
    }

    public void setNormalTextSize(int mNormalTextSize) {
        this.mNormalTextSize = mNormalTextSize;
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
    }
}
