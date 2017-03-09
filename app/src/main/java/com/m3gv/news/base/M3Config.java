package com.m3gv.news.base;

import android.app.Activity;
import android.app.Application;

import com.m3gv.news.common.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by meikai on 16/12/6.
 */

public class M3Config {

    private static final String TAG = "M3Config";
    private static final ReentrantLock INITIALIZED_LOCK = new ReentrantLock();
    private static boolean initialized;

    private static Application application;

    private static WeakReference<Activity> currentActivity;


    public static void init(Application application) {
        if (!INITIALIZED_LOCK.tryLock()) {
            return;
        }
        try {
            if (initialized) {
                LogUtil.d(TAG, "M3Config has already initialized");
                return;
            }
            initialized = true;
        } finally {
            INITIALIZED_LOCK.unlock();
        }

        M3Config.application = application;
    }

    public static Application getContext() {
        return application;
    }

    static void setCurrentActivity(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    public static Activity getCurrentActivity() {
        return currentActivity != null ? currentActivity.get() : null;
    }

    public static boolean isDebug() {
        return true;
    }


}
