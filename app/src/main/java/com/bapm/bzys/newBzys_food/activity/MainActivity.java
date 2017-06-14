package com.bapm.bzys.newBzys_food.activity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.bapm.bzys.newBzys_food.jiguang.ExampleUtil;
import com.bapm.bzys.newBzys_food.jiguang.LocalBroadcastManager;
import com.bapm.bzys.newBzys_food.network.DadanUrl;
import com.bapm.bzys.newBzys_food.network.function.interf.Function;
import com.bapm.bzys.newBzys_food.network.function.interf.FunctionManager;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.bapm.bzys.newBzys_food.util.Constants;
import com.bapm.bzys.newBzys_food.util.CustomToast;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.widget.dialog.MyDialog;
import com.bapm.bzys.newBzys_food.widget.dialog.MyDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity  implements Function{
	private FunctionManager manager;
	// 布局管理器
	private FragmentManager fragManager;

	private FragmentHome  fragHome;
	private FragmentOrder fragOrder;

	// 主页
	private ImageView iv_menu_home;
	private TextView tv_menu_home;

	// 订单
	private ImageView iv_menu_order;
	private TextView tv_menu_order;
	private int intPromotionID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		manager = this.init(this.getContext());
		manager.registFunClass(MainActivity.class);
		// 初始化组件
		initViews();
		// 默认先点中第一个“首页”
		if (getIntent().getStringExtra("isOrder")!=null&&getIntent().getStringExtra("isOrder").equals("order")) {
			intPromotionID=getIntent().getIntExtra("intPromotionID",0);
			clickMenu(findViewById(R.id.layout_menu_order));
			intPromotionID=0;
		}else {
			clickMenu(findViewById(R.id.layout_menu_home));
		}

		registerMessageReceiver();
		getId();
//		setStyleBasic();
		setStyleCustom();
	}
	/**
	 * 设置通知提示方式 - 基础属性
	 */
	private void setStyleBasic() {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MainActivity.this);
		builder.statusBarDrawable = R.mipmap.icon;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
		JPushInterface.setPushNotificationBuilder(1, builder);
//		Toast.makeText(ActivityConnectBuleTooth.this, "Basic Builder - 1", Toast.LENGTH_SHORT).show();
	}


	/**
	 * 设置通知栏样式 - 定义通知栏Layout
	 */
	private void setStyleCustom() {
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(MainActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title,0);
		builder.statusBarDrawable = R.mipmap.ic_launcher;
		builder.developerArg0 = "developerArg2";
		JPushInterface.setPushNotificationBuilder(2, builder);
//		Toast.makeText(ActivityConnectBuleTooth.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
	}
	private void getId() {
		JSONObject params=new JSONObject();
		try {
			params.put("registrationId", JPushInterface.getRegistrationID(getApplicationContext()));
			Log.e("ddd",JPushInterface.getRegistrationID(getApplicationContext()));
			manager.GetRegistrationId(params,MainActivity.this);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}
	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static boolean isForeground = false;
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
					String messge = intent.getStringExtra(KEY_MESSAGE);
					String extras = intent.getStringExtra(KEY_EXTRAS);
					StringBuilder showMsg = new StringBuilder();
					showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
					if (!ExampleUtil.isEmpty(extras)) {
						showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
					}
				}
			} catch (Exception e){
			}
		}
	}
	private void initViews() {
		// 布局管理器
		fragManager = getSupportFragmentManager();

		iv_menu_home = (ImageView)findViewById(R.id.iv_menu_home);
		tv_menu_home = (TextView)findViewById(R.id.tv_menu_home);

		iv_menu_order = (ImageView)findViewById(R.id.iv_menu_order);
		tv_menu_order = (TextView)findViewById(R.id.tv_menu_order);
	}
	@Override
	public FunctionManager init(Context context) {
		return new FunctionManager(context);
	}
	/**
	 * 点击底部菜单事件
	 * @param v
	 */
	public void clickMenu(View v){
		FragmentTransaction trans = fragManager.beginTransaction();
		int vID = v.getId();
		// 设置menu样式
		setMenuStyle(vID);
		// 隐藏所有的fragment
		hideFrament(trans);
		// 设置Fragment
		setFragment(vID,trans);
		if (intPromotionID!=0) {
			Bundle bundle = new Bundle();
			bundle.putInt("intPromotionID",getIntent().getIntExtra("intPromotionID",0));
			fragOrder.setArguments(bundle);
		}else{

		}
		trans.commit();
	}
	/**
	 * 隐藏所有的fragment(编程初始化状态)
	 * @param trans
	 */
	private void hideFrament(FragmentTransaction trans) {
		if(fragHome!=null){
			trans.hide(fragHome);
		}
		if(fragOrder!=null){
			trans.hide(fragOrder);
		}
	}
	/**
	 * 设置menu样式
	 * @param vID
	 * @param trans
	 */
	private void setMenuStyle(int id) {
		// 主页
		if(id==R.id.layout_menu_home){
			iv_menu_home.setImageDrawable(getResources().getDrawable(R.mipmap.menu_home_click));
			tv_menu_home.setTextColor(getResources().getColor(R.color.menu_click));
		}
		else {
			iv_menu_home.setImageDrawable(getResources().getDrawable(R.mipmap.menu_home_nomarl));
			tv_menu_home.setTextColor(getResources().getColor(R.color.menu_nomarl));
		}
		// 订单
		if(id==R.id.layout_menu_order){
			iv_menu_order.setImageDrawable(getResources().getDrawable(R.mipmap.menu_order_click));
			tv_menu_order.setTextColor(getResources().getColor(R.color.menu_click));
		}else {
			iv_menu_order.setImageDrawable(getResources().getDrawable(R.mipmap.menu_order_nomarl));
			tv_menu_order.setTextColor(getResources().getColor(R.color.menu_nomarl));
		}
	}

	/**
	 * 设置Fragment
	 * @param vID
	 * @param trans
	 */
	private void setFragment(int vID,FragmentTransaction trans) {
		switch (vID) {
			case R.id.layout_menu_home:
				if(fragHome==null){
					fragHome = new FragmentHome();
					trans.add(R.id.content,fragHome);
				}else{
					trans.show(fragHome);
				}
				break;
			case R.id.layout_menu_order:
				if(fragOrder==null){
					fragOrder = new FragmentOrder();
					trans.add(R.id.content,fragOrder);
				}else{
					trans.show(fragOrder);
				}
				break;
			default:
				break;
		}
	}
	@Override
	public void onBackPressed() {
		new MyDialog(this.getContext()).callback(MyDialog.TYPE_INFO, "您确定要退出登录吗？",
				new MyDialogListener() {
					@Override
					public void callback(String[] array) {
						Map<String, String> params = new HashMap<String, String>();
						manager.exit(params, MainActivity.this);

						Intent intent = new Intent(MainActivity.this,LoginActivity.class);
						startActivity(intent);
						DadanPreference.getInstance(MainActivity.this).removeTicket();
						ActivityManager.getInstance().finishAllActivity();
					}
				}, "确定",
				new MyDialogListener() {
					@Override
					public void callback(String[] array) {

					}
				},"取消");
	}
	@Override
	public void onSuccess(int requstCode, JSONObject result) {
		switch (requstCode) {
			case DadanUrl.EXIT_URL_CODE:{
				try {
					int code = result.optInt(Constants.CODE_KEY);
					String message = result.getString(Constants.MESSAGE_KEY);
					if(code==Constants.NETWORK_SUCCESS){
//					Intent intent = new Intent(ActivityConnectBuleTooth.this,LoginActivity.class);
//					startActivity(intent);
//					DadanPreference.getInstance(ActivityConnectBuleTooth.this).removeTicket();
//					ActivityConnectBuleTooth.this.finish();
					}else{
						 CustomToast.showToast(MainActivity.this, message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
			case DadanUrl.GET_REGISTRATION_CODE:
				try {
					Log.e("ddd",result.getString("Message"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			default:
				break;
		}
	}
	@Override
	public void onSuccess(int requstCode, JSONArray result) {

	}
	@Override
	public void onFaile(int requestCode, int status, String msg) {
//		 CustomToast.showToast(this,msg,Toast.LENGTH_LONG).show();

	}
}

