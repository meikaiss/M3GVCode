package com.m3gv.news.common.view.xrecyclerview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m3gv.news.R;


public class PullRefreshHeader extends LinearLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_RELEASE_TO_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;
    public static final int STATE_DONE = 3;


    private LinearLayout mContainer;
    private TextView mStatusTextView;
    private ImageView mRefreshImageView;

    private int mCurrentState = STATE_NORMAL;

    public int mMeasuredHeight;


    public PullRefreshHeader(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public PullRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.xrecyclerview_refresh_header, this, false);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);

        this.setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));

        mStatusTextView = (TextView) findViewById(R.id.refresh_status_textview);
        mRefreshImageView = (ImageView) findViewById(R.id.refresh_image);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    public void setRefreshImageViewBg(@DrawableRes int backgroundResId) {
        if (mRefreshImageView != null) {
            mRefreshImageView.setBackgroundResource(backgroundResId);
        }
    }

    public void setState(int state) {
        if (state == mCurrentState) {
            return;
        }

        switch (state) {
            case STATE_NORMAL:
                mStatusTextView.setText("下拉刷新");
                break;
            case STATE_RELEASE_TO_REFRESH:
                mStatusTextView.setText("释放立即刷新");
                break;
            case STATE_REFRESHING:
                if (mRefreshImageView.getBackground() != null
                        && mRefreshImageView.getBackground() instanceof AnimationDrawable) {
                    ((AnimationDrawable) mRefreshImageView.getBackground()).start();
                }
                mStatusTextView.setText("正在刷新...");
                break;
            case STATE_DONE: {
                if (mRefreshImageView.getBackground() != null
                        && mRefreshImageView.getBackground() instanceof AnimationDrawable) {
                    ((AnimationDrawable) mRefreshImageView.getBackground()).stop();
                }
                mStatusTextView.setText("刷新完成");
            }
            break;
            default:
        }

        mCurrentState = state;
    }

    public int getState() {
        return mCurrentState;
    }

    public void refreshComplete() {
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 200);
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mCurrentState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) {
            isOnRefresh = false;
        }

        if (getVisibleHeight() > mMeasuredHeight && mCurrentState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mCurrentState == STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mCurrentState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        if (mRefreshImageView.getBackground() != null){
            AnimationDrawable drawable = (AnimationDrawable) mRefreshImageView.getBackground();
            drawable.selectDrawable(0);
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    public void startToRefresh(Animator.AnimatorListener animatorListener) {
        smoothScrollTo(mMeasuredHeight, animatorListener);
    }

    private void smoothScrollTo(int destHeight) {
        smoothScrollTo(destHeight, null);
    }

    private void smoothScrollTo(final int destHeight, final Animator.AnimatorListener animatorListener) {
        smoothScrollTo(destHeight, animatorListener, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
    }

    private void smoothScrollTo(int destHeight, final Animator.AnimatorListener animatorListener,
            ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        if (animatorListener != null) {
            animator.addListener(animatorListener);
        }
        animator.addUpdateListener(animatorUpdateListener);
        animator.start();
    }

}