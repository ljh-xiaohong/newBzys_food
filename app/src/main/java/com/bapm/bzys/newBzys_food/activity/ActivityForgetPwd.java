package com.bapm.bzys.newBzys_food.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.bapm.bzys.newBzys_food.network.DadanUrl;
import com.bapm.bzys.newBzys_food.network.HttpUtil;
import com.bapm.bzys.newBzys_food.network.function.interf.Function;
import com.bapm.bzys.newBzys_food.network.function.interf.FunctionManager;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.bapm.bzys.newBzys_food.util.CommonUtil;
import com.bapm.bzys.newBzys_food.util.Constants;
import com.bapm.bzys.newBzys_food.util.CountDownTimerUtils;
import com.bapm.bzys.newBzys_food.util.CustomToast;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.util.LoginFailUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityForgetPwd extends BaseActivity implements Function{
	private FunctionManager manager;
	private EditText ed_phone;
	private EditText ed_code;
	private LoginFailUtils failUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		ed_phone      = (EditText) findViewById(R.id.ed_phone);
		ed_code       = (EditText) findViewById(R.id.ed_code);
		
		manager = this.init(this.getContext());
		manager.registFunClass(ActivityForgetPwd.class);
	}
	public void validateCode(View view){
		String phone = ed_phone.getText().toString();
		if(!CommonUtil.checkPhoneNum(phone)){
			CustomToast.showToast(this,"请正确输入手机号!");
		}else {
			loadDialog.show();
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone", phone);
			CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils((Button) view, 60000, 1000);
			mCountDownTimerUtils.start();
			manager.forgetValidateCode(params, this);
		}
	}
	public void forget(View v){
		String phone = ed_phone.getText().toString();
		if(!CommonUtil.checkPhoneNum(phone)){
			CustomToast.showToast(this,"请正确输入手机号!");
		}else if (CommonUtil.isNull(ed_code.getText().toString())){
			CustomToast.showToast(this,"验证码不能为空");
		}else {
			loadDialog.show();
			Map<String, String> params = new HashMap<String, String>();
			params.put("validateCode", ed_code.getText().toString());
			params.put("Phone", ed_phone.getText().toString());
			manager.forget(params, this);
		}
	}
	@Override
	public FunctionManager init(Context context) {
		return new FunctionManager(context);
	}
	@Override
	public void onSuccess(int requstCode, JSONObject result) {
		loadDialog.dismiss();
		switch (requstCode) {
			case DadanUrl.USER_LOGIN_AGAIN_REQUEST_CODE:
				if (result.optString("LogionCode").equals("1")) {
					DadanPreference.getInstance(this).setTicket(result.optString("Ticket"));
					failUtils.getId();
				}else if(result.optString("LogionCode").equals("-1")){
					Intent intent=new Intent(this,LoginActivity.class);
					intent.putExtra("LogionCode","-1");
					startActivity(intent);
					ActivityManager.getInstance().finishAllActivity();
				}
				break;
		case DadanUrl.USER_VALIDATE_URL_CODE:{
			try{
				int loginCode = result.optInt(Constants.CODE_KEY);
				String message = result.getString(Constants.MESSAGE_KEY);
				if(loginCode==Constants.NETWORK_SUCCESS){
					 CustomToast.showToast(this, message);
				}else{
					 CustomToast.showToast(this, message);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		}
		case DadanUrl.VALIDATE_CODE_URL_CODE:{
			try{
				int loginCode = result.optInt(Constants.CODE_KEY);
				String message = result.getString(Constants.MESSAGE_KEY);
				if(loginCode==Constants.NETWORK_SUCCESS){
					Intent intent = new Intent(this,ActivityForgetPwdNext.class);
					intent.putExtra("phone",ed_phone.getText().toString());
					this.startActivity(intent);
					this.finish();
				}else{
					 CustomToast.showToast(this, message);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		}
		default:
			break;
		}
	}

	public void back(View v){
		this.finish();
	}
	@Override
	public void onFaile(int requestCode, int status, String msg) {
		loadDialog.dismiss();
		Log.i(ActivityForgetPwd.class.toString(),msg);
//		 CustomToast.showToast(this,msg,Toast.LENGTH_LONG).show();
		loadDialog.dismiss();
		failUtils=new LoginFailUtils(requestCode,ActivityForgetPwd.this ,manager,ActivityForgetPwd.this);
		failUtils.onFaile();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.unregistFunctionClass(ActivityForgetPwd.class);
	}
	@Override
	public void onSuccess(int requstCode, JSONArray result) {
		loadDialog.dismiss();
	}
}
