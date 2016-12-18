package com.m3gv.news.common.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.m3gv.news.M3gApplication;

/**
 * Created by meikai on 16/12/18.
 */

public class UIUtil {

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void showToast(String message) {
        showToast(M3gApplication.application, message);
    }

    public static void showToast(final Context context, final String message) {
        mainHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
