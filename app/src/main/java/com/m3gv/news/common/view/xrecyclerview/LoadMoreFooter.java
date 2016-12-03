package com.m3gv.news.common.view.xrecyclerview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.m3gv.news.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class LoadMoreFooter extends LinearLayout {

    public static final int STATE_LOADING = 0;
    public static final int STATE_COMPLETE = 1;
    public static final int STATE_COMPLETE_NO_MORE = 2;
    public static final int STATE_LOAD_ERROR = 3;

    @IntDef({STATE_LOADING, STATE_COMPLETE, STATE_COMPLETE_NO_MORE, STATE_LOAD_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FooterState {
    }

    private int currentState;

    private TextView footerTextView;
    private ProgressBar progressBar;

    private OnReloadClickListener onReloadClickListener;

    public void setOnReloadListener(OnReloadClickListener onReloadClickListener) {
        this.onReloadClickListener = onReloadClickListener;
    }

    public interface OnReloadClickListener {
        void onReload();
    }

    public LoadMoreFooter(Context context) {
        super(context);
        initView();
    }

    public
    @FooterState
    int getCurrentState() {
        return currentState;
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(lp);

        LayoutInflater.from(getContext())
                .inflate(R.layout.xrecyclerview_loadmore_footer, this);

        footerTextView = (TextView) findViewById(R.id.x_recycler_view_footer_tv);
        footerTextView.setText("松开加载...");

        footerTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentState == STATE_LOAD_ERROR) {
                    // 重新加载
                    if (onReloadClickListener != null) {
                        onReloadClickListener.onReload();
                    }
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.load_more_foot_progress);
    }

    public void setState(@FooterState int state) {
        this.currentState = state;
        switch (state) {
            case STATE_LOADING:
                footerTextView.setText(R.string.x_recycler_view_loading);
                progressBar.setVisibility(View.VISIBLE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                footerTextView.setText(R.string.x_recycler_view_load_complete);
                progressBar.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE_NO_MORE:
                footerTextView.setText(R.string.x_recycler_view_complete_no_more);
                progressBar.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_LOAD_ERROR:
                footerTextView.setText(R.string.x_recycler_view_load_failed);
                progressBar.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


}
