package com.m3gv.news.base;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by meikai on 16/12/6.
 */

public class M3Config {

    private static WeakReference<Activity> currentActivity;

    static void setCurrentActivity(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    public static Activity getCurrentActivity() {
        return currentActivity != null ? currentActivity.get() : null;
    }

    public static boolean isDebug(){
        return true;
    }



}
