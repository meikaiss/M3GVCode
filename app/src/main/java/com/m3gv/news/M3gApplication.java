package com.m3gv.news;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by meikai on 16/12/3.
 */

public class M3gApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"VKBQghTRDUmF2zhvEtJrRgDD-gzGzoHsz","0RnczmpwiB0uRefLRVfIvXEQ");

    }
}
