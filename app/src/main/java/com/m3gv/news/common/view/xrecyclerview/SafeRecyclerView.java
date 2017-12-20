package com.m3gv.news.common.view.xrecyclerview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class SafeRecyclerView extends RecyclerView {
    public SafeRecyclerView(Context context) {
        super(context);
    }

    public SafeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SafeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return getLayoutManager() == null ? 0 : super.computeHorizontalScrollOffset();
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return getLayoutManager() == null ? 0 : super.computeHorizontalScrollExtent();
    }

    @Override
    public int computeHorizontalScrollRange() {
        return getLayoutManager() == null ? 0 : super.computeHorizontalScrollRange();
    }

    @Override
    public int computeVerticalScrollOffset() {
        return getLayoutManager() == null ? 0 : super.computeVerticalScrollOffset();
    }

    @Override
    public int computeVerticalScrollExtent() {
        return getLayoutManager() == null ? 0 : super.computeVerticalScrollExtent();
    }

    @Override
    public int computeVerticalScrollRange() {
        return getLayoutManager() == null ? 0 : super.computeVerticalScrollRange();
    }

}