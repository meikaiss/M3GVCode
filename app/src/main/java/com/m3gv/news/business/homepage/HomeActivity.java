package com.m3gv.news.business.homepage;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.business.video.VideoListFragment;
import com.m3gv.news.common.util.AppUtil;
import com.m3gv.news.common.util.NetUtil;

/**
 * Created by meikai on 16/12/10.
 */
public class HomeActivity extends M3gBaseActivity implements View.OnClickListener {

    private TextView tvMenu1;
    private TextView tvMenu2;
    private TextView tvMenu3;
    private TextView tvMenu4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_frame_container, VideoListFragment.newInstance()).commitAllowingStateLoss();

        tvMenu1 = f(R.id.home_menu_tv_1);
        tvMenu2 = f(R.id.home_menu_tv_2);
        tvMenu3 = f(R.id.home_menu_tv_3);
        tvMenu4 = f(R.id.home_menu_tv_4);

        tvMenu1.setOnClickListener(this);
        tvMenu2.setOnClickListener(this);
        tvMenu3.setOnClickListener(this);
        tvMenu4.setOnClickListener(this);

        tvMenu1.setSelected(true);

        //收集用户信息
        submitUserInfo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_menu_tv_1:

                break;
            case R.id.home_menu_tv_2:

                break;
            case R.id.home_menu_tv_3:

                break;
            case R.id.home_menu_tv_4:

                break;
            default:
                break;
        }
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
