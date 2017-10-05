package com.hengweather.android.application;

import android.app.Application;

import com.hengweather.android.util.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 项目名：hengweather
 * 包名：  com.hengweather.android.application
 * 文件名：BaseApplication
 * Created by liushengjie on 2017/9/25.
 * 描述：     基础类
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化bugly
        //测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, false);
    }
}
