package com.m3gv.news.common.webview;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.m3gv.news.base.M3Config;

/**
 * Created by meikai on 16/12/17.
 */
public class M3ProtocolV1ProcessorImpl implements IM3ProtocolV1Processor {

    @Override
    public String version() {
        return "型号" + android.os.Build.MODEL + ", 安卓版本" + android.os.Build.VERSION.RELEASE;
    }

    @Override
    public void toast(String message) {
        Toast.makeText(M3Config.getCurrentActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void log(String message) {
        Log.e("M3WebView", "message = " + message);
    }

    @Override
    public void action(String message) {
        Intent intent = new Intent(message);
        if (M3Config.getCurrentActivity() != null) {
            M3Config.getCurrentActivity().startActivity(intent);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            M3Config.getCurrentActivity().startActivity(intent);
        }
    }
}
