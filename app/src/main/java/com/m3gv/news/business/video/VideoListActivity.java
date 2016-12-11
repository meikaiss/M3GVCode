package com.m3gv.news.business.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.util.AppUtil;
import com.m3gv.news.common.util.NetUtil;

/**
 * Created by meikai on 16/12/4.
 */

public class VideoListActivity extends M3gBaseActivity {

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, VideoListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_activity);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, VideoListFragment.newInstance())
                .commit();

        //收集用户信息
        submitUserInfo();
    }

    private void submitUserInfo() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        AVObject userInfo = new AVObject("UserInfo");
        userInfo.put("appVersion", AppUtil.getVersionName(this));
        userInfo.put("netType", NetUtil.isWifi(this) ? "wifi" : "4G");
        userInfo.put("MANUFACTURER", Build.MANUFACTURER);
        userInfo.put("BOARD", Build.BOARD);
        userInfo.put("MODEL", Build.MODEL);
        userInfo.put("SDK_VERSION", Build.VERSION.SDK);
        userInfo.put("SDK_INT", Build.VERSION.SDK_INT);
        if (tm.getDeviceId() != null) {
            userInfo.put("IMEI", tm.getDeviceId());
        } else {
            userInfo.put("IMEI", "null");
        }

        userInfo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.d("saved", "success!");
                }
            }
        });
    }

}
