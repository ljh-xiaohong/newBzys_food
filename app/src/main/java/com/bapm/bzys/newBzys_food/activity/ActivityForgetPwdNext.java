package com.bapm.bzys.newBzys_food.activity;

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
import com.bapm.bzys.newBzys_food.util.CustomToast;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.util.LoginFailUtils;
import com.bapm.bzys.newBzys_food.widget.dialog.MyDialog;
import com.bapm.bzys.newBzys_food.widget.dialog.MyDialogListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class ActivityForgetPwdNext extends BaseActivity implements Function{
	private FunctionManager manager;
	private EditText ed_new_pwd;
	private EditText ed_again_pwd;
	private String phone;
	private LoginFailUtils failUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd_next);
		ed_new_pwd        = (EditText) findViewById(R.id.ed_new_pwd);
		ed_again_pwd        = (EditText) findViewById(R.id.ed_again_pwd);
		if(getIntent().hasExtra("phone")){
			phone = getIntent().getStringExtra("phone");
		}
		manager = this.init(this.getContext());
		manager.registFunClass(ActivityForgetPwdNext.class);
	}
	public void resetPwd(View view){
		try {
			String newPwd = ed_new_pwd.getText().toString();
			String replyPwd = ed_again_pwd.getText().toString();
			if (CommonUtil.isNull(newPwd)||CommonUtil.isNull(replyPwd)){
				CustomToast.showToast(this,"密码不能为空");
			}else {
				loadDialog.show();
				JSONObject params = new JSONObject();
				params.put("phone", phone);
				params.put("newPsd", newPwd);
				params.put("confimPsd", replyPwd);
				manager.resetPwd(params, this);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public FunctionManager init(Context context) {
		return new FunctionManager(context);
	}
	@Override
	public void onSuccess(int requstCode, JSONObject result) {
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
		case DadanUrl.USER_RESET_PWD_URL_CODE:{
			try{
				int loginCode = result.optInt(Constants.CODE_KEY);
				String message = result.getString(Constants.MESSAGE_KEY);
				if(loginCode==Constants.NETWORK_SUCCESS){
					new MyDialog(this.getContext()).callback(MyDialog.TYPE_INFO,message,
							new MyDialogListener() {
								@Override
								public void callback(String[] array) {
									ActivityForgetPwdNext.this.finish();
								}
							}, "确定", 
							null,null);
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
		loadDialog.dismiss();
	}
	public void back(View v){
		this.finish();
	}
	@Override
	public void onFaile(int requestCode, int status, String msg) {
		loadDialog.dismiss();
		Log.i(ActivityForgetPwdNext.class.toString(),msg);
		failUtils=new LoginFailUtils(requestCode,ActivityForgetPwdNext.this ,manager,ActivityForgetPwdNext.this);
		failUtils.onFaile();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		loadDialog.dismiss();
		manager.unregistFunctionClass(ActivityForgetPwdNext.class);
	}
	@Override
	public void onSuccess(int requstCode, JSONArray result) {
		loadDialog.dismiss();
	}
}
