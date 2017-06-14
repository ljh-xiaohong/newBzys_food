package com.bapm.bzys.newBzys_food.base;


import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;

/**
 * Created by fs-ljh on 2017/4/24.
 */

public class UseDeviceSizeApplication extends Application {
    private static final String[] CERTIFICATES = new String[]{"fsmsAPI.hxfsjt.com.cer"};
    //不管是蓝牙连接方还是服务器方，得到socket对象后都传入
    public static BluetoothSocket bluetoothSocket;
    @Override
    public void onCreate()
    {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        ShareSDK.initSDK(this,"1e616350036a0");
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(getCertificates(this, CERTIFICATES),null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
    /**
     * 获取服务端证书
     * <p>
     * 默认放在Assets目录下
     *
     * @param context
     * @return
     */
    public static InputStream[] getCertificates(Context context, String... fileNames) {
        if (context == null || fileNames == null || fileNames.length <= 0) {
            return null;
        }
        try {
            InputStream[] certificates = new InputStream[fileNames.length];
            for (int i = 0; i < fileNames.length; i++) {
                certificates[i] = context.getAssets().open(fileNames[i]);
            }
//			Log.e("certificates",certificates);
            return certificates;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
