package com.m3gv.news;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.m3gv.news.base.M3Config;
import com.m3gv.news.business.db.M3DB;
import com.m3gv.news.common.realmdb.RealmDbHelper;
import com.m3gv.news.common.util.LogUtil;
import com.m3gv.news.common.util.SystemUtil;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by meikai on 16/12/3.
 */

public class M3gApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        long start = System.currentTimeMillis();
        M3Config.init(this);

        initBugly();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "VKBQghTRDUmF2zhvEtJrRgDD-gzGzoHsz", "0RnczmpwiB0uRefLRVfIvXEQ");

        RealmDbHelper.getInstance().init(this);

        M3DB.getInstance().init();

        long end = System.currentTimeMillis();
        LogUtil.e("M3gApplication", "onCreate耗时=" + (end - start));
    }


    /**
     * 只在主进程下上报数据：判断是否是主进程（通过进程名是否为包名来判断）
     */
    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = SystemUtil.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "45ff042175",
                getResources().getBoolean(R.bool.open_bugly_in_debug), strategy);
    }


}
