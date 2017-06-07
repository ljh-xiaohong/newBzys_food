package com.bapm.bzys.newBzys_food.base;


import android.app.Application;
import com.zhy.autolayout.config.AutoLayoutConifg;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by fs-ljh on 2017/4/24.
 */

public class UseDeviceSizeApplication extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        ShareSDK.initSDK(this,"1e616350036a0");
    }
}
