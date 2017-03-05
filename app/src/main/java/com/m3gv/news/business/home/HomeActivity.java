package com.m3gv.news.business.home;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.m3gv.news.R;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.base.M3gBaseFragment;
import com.m3gv.news.business.cartoon.CartoonListFragment;
import com.m3gv.news.common.util.AppUtil;
import com.m3gv.news.common.util.NetUtil;

/**
 * Created by meikai on 16/12/10.
 */
public class HomeActivity extends M3gBaseActivity implements View.OnClickListener {

    private TextView[] tvMenuArr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        }

        tvMenuArr = new TextView[4];
        tvMenuArr[0] = f(R.id.home_menu_tv_0);
        tvMenuArr[1] = f(R.id.home_menu_tv_1);
        tvMenuArr[2] = f(R.id.home_menu_tv_2);
        tvMenuArr[3] = f(R.id.home_menu_tv_3);

        tvMenuArr[0].setOnClickListener(this);
        tvMenuArr[1].setOnClickListener(this);
        tvMenuArr[2].setOnClickListener(this);
        tvMenuArr[3].setOnClickListener(this);

        tabMenuClick(0);

        //收集用户信息
        submitUserInfo();
    }

    private int currentTabIndex = -1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_menu_tv_0:
                tabMenuClick(0);
                break;
            case R.id.home_menu_tv_1:
                tabMenuClick(1);
                break;
            case R.id.home_menu_tv_2:
                tabMenuClick(2);
                break;
            case R.id.home_menu_tv_3:
                tabMenuClick(3);
                break;
            default:
                break;
        }
    }

    private void tabMenuClick(int clickIndex) {
        if (currentTabIndex == clickIndex) {
            return;
        }
        //显示 新的
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("tab_" + clickIndex);
        if (fragment == null) {
            fragment = createTabFragment(clickIndex);
            getSupportFragmentManager().beginTransaction().add(R.id.home_frame_container, fragment, "tab_" + clickIndex)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        }
        tvMenuArr[clickIndex].setSelected(true);

        //取消 旧的
        if (currentTabIndex >= 0) {
            Fragment fragment0 = getSupportFragmentManager().findFragmentByTag("tab_" + currentTabIndex);
            if (fragment0 != null) {
                getSupportFragmentManager().beginTransaction().hide(fragment0).commitAllowingStateLoss();
            }

            tvMenuArr[currentTabIndex].setSelected(false);
        }

        //更新数据
        currentTabIndex = clickIndex;
    }

    private M3gBaseFragment createTabFragment(int index) {
        M3gBaseFragment fragment = null;
        switch (index) {
            case 0:
                fragment = HomeVideoFragment.newInstance();
                break;
            case 1:
                fragment = HomeArticleFragment.newInstance();
                break;
            case 2:
                fragment = CartoonListFragment.newInstance("Cartoon");
                break;
            case 3:
                fragment = HomeDataFragment.newInstance();
                break;
            default:
                break;
        }
        return fragment;
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

        String imei;
        if (tm.getDeviceId() != null) {
            imei = tm.getDeviceId();
        } else {
            imei = "null";
        }

        userInfo.put("IMEI", imei);
        if ("861414038624178".equals(imei)) {
            return;
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
