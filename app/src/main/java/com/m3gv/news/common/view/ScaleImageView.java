package com.m3gv.news.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.m3gv.news.R;

/**
 * Created by meikai on 16/12/26.
 */
public class ScaleImageView extends ImageView {

    private float widthScale = 1;
    private float heightScale = 1;

    public ScaleImageView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView, defStyleAttr, defStyleRes);
        widthScale = arr.getFloat(R.styleable.ScaleImageView_width_scale, 1f);
        heightScale = arr.getFloat(R.styleable.ScaleImageView_height_scale, 1f);
        arr.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (widthSize * heightScale / widthScale);

        this.setMeasuredDimension(widthSize, height);
    }
}
