package com.m3gv.news.business.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.m3gv.news.common.view.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;

/**
 * Created by meikai on 17/1/25.
 */

public class SkillPagerTitleView extends RelativeLayout implements IMeasurablePagerTitleView {


    private ImageView skillImageView;

    public SkillPagerTitleView(Context context) {
        super(context);

        skillImageView = new ImageView(context);
        skillImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(CENTER_IN_PARENT);
        int dp10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, Resources.getSystem().getDisplayMetrics());
        lp.setMargins(dp10, dp10, dp10, dp10);

        addView(skillImageView, lp);
    }

    public ImageView getSkillImageView() {
        return skillImageView;
    }

    @Override
    public int getContentLeft() {
        return 0;
    }

    @Override
    public int getContentTop() {
        return 0;
    }

    @Override
    public int getContentRight() {
        return 0;
    }

    @Override
    public int getContentBottom() {
        return 0;
    }


    @Override
    public void onSelected(int index, int totalCount) {

    }

    @Override
    public void onDeselected(int index, int totalCount) {

    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }

}
