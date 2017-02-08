package com.m3gv.news;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.business.homepage.HomeActivity;
import com.m3gv.news.business.youmiAd.PermissionHelper;
import com.m3gv.news.common.util.LogUtil;

import net.youmi.android.AdManager;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

/**
 * Created by meikai on 17/1/31.
 */

public class SplashActivity extends M3gBaseActivity {

    protected static final String TAG = "youmi-demo";

    protected Context mContext;

    private PermissionHelper mPermissionHelper;

    /**
     * 跑应用的逻辑
     */
    private void runApp() {
        //初始化SDK
        AdManager.getInstance(mContext).init("a182bedcefb2b082", "8b2aba7abaf9d605", false, false);

        //设置开屏
        setupSplashAd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 移除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {
        // 创建开屏容器
        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ABOVE, R.id.view_divider);

        // 对开屏进行设置
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        // 设置是否 展示失败自动跳转，默认自动跳转
        splashViewSettings.setAutoJumpToTargetWhenShowFailed(true);
        // 设置跳转的窗口类
        splashViewSettings.setTargetClass(HomeActivity.class);
        // 设置开屏的容器
        splashViewSettings.setSplashViewContainer(splashLayout);

        // 展示开屏广告
        SpotManager.getInstance(mContext)
                .showSplash(mContext, splashViewSettings, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        LogUtil.e("", "开屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        LogUtil.e("", "开屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                LogUtil.e("", "网络异常");
                                break;
                            case ErrorCode.NON_AD:
                                LogUtil.e("", "暂无开屏广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                LogUtil.e("", "开屏资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                LogUtil.e("", "开屏展示间隔限制");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                LogUtil.e("", "开屏控件处在不可见状态");
                                break;
                            default:
                                LogUtil.e("", String.format("errorCode: %d", errorCode));
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        LogUtil.e("", "开屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        LogUtil.e("", "开屏被点击");
                        LogUtil.e("", String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 开屏展示界面的 onDestroy() 回调方法中调用
        SpotManager.getInstance(mContext).onDestroy();
    }

}
