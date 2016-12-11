package com.m3gv.news.business.video;

import android.animation.ObjectAnimator;

/**
 * 解决tip退场动画的时序控制问题
 * Created by meikai on 16/12/4.
 */
public class RefreshTipRunnable implements Runnable {

    boolean terminateFlag = false;

    ObjectAnimator hideRefreshTipAnimator = null;

    @Override
    public void run() {

    }

}
