package com.bapm.bzys.newBzys_food.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bapm.bzys.newBzys_food.activity.MainActivity;
import com.bapm.bzys.newBzys_food.network.HttpUtil;
import com.bapm.bzys.newBzys_food.network.function.interf.Function;
import com.bapm.bzys.newBzys_food.network.function.interf.FunctionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by fs-ljh on 2017/6/13.
 */

public class LoginFailUtils {
    private int requestCode;
    private  Context context;
    private  FunctionManager manager;
    private  Function function;

    public LoginFailUtils() {

    }

    public LoginFailUtils(int requestCode, Context context, FunctionManager manager, Function function) {
        this.requestCode=requestCode;
        this.context=context;
        this.manager=manager;
        this.function=function;
    }

    public void onFaile() {
        if(requestCode== HttpUtil.ST_ACCOUNT_OTHER_LOGIN_FAILE||requestCode==233){
            Map<String, String> params = new HashMap<String, String>();
            params.put("DEVICE_ID", ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE)).getDeviceId());
            manager.loginAgain(params, function);
//            getId(context,manager,function);
        }
    }
    public void getId(){
        JSONObject params=new JSONObject();
        try {
            params.put("registrationId", JPushInterface.getRegistrationID(context.getApplicationContext()));
            Log.e("ddd",JPushInterface.getRegistrationID(context));
            manager.GetRegistrationId(params,function);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
