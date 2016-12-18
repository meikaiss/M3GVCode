package com.m3gv.news.business;

import android.animation.ObjectAnimator;

/**
 * 解决tip退场动画的时序控制问题
 * Created by meikai on 16/12/4.
 */
public class RefreshTipRunnable implements Runnable {

    public boolean terminateFlag = false;

    public ObjectAnimator hideRefreshTipAnimator = null;

    @Override
    public void run() {

    }

}
