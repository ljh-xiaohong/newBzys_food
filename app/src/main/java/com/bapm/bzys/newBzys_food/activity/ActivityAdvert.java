package com.bapm.bzys.newBzys_food.activity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.adapter.AdvertAdapter;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.bapm.bzys.newBzys_food.model.Advert;
import com.bapm.bzys.newBzys_food.model.StoreMessage;
import com.bapm.bzys.newBzys_food.network.DadanUrl;
import com.bapm.bzys.newBzys_food.network.HttpUtil;
import com.bapm.bzys.newBzys_food.network.function.interf.Function;
import com.bapm.bzys.newBzys_food.network.function.interf.FunctionManager;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.bapm.bzys.newBzys_food.util.CustomToast;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.util.FileUtils;
import com.bapm.bzys.newBzys_food.util.GlideUtils;
import com.bapm.bzys.newBzys_food.widget.ZrcListView;
import com.bapm.bzys.newBzys_food.widget.dialog.TipsDialog;
import com.bapm.bzys.newBzys_food.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ActivityAdvert extends BaseActivity implements Function,OnClickListener,ZrcListView.OnItemClickListener {
	private FunctionManager manager;
	private Button btn_add;
	private ZrcListView listView;
	private AdvertAdapter adapter;
	private List<Advert> list;
	private LinearLayout   layout_menu_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advert);
		manager = this.init(this.getContext());
		manager.registFunClass(ActivityAdvert.class);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initView();
		initData();
	}

	@Override
	public FunctionManager init(Context context) {
		return new FunctionManager(context);
	}
	public void initView(){
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);
		layout_menu_home = (LinearLayout) findViewById(R.id.layout_menu_home);
		layout_menu_home.setOnClickListener(this);
		listView = (ZrcListView) findViewById(R.id.zListView);
		list = new ArrayList<Advert>();
		adapter = new AdvertAdapter(this,list);
//		// 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
//		float density = getResources().getDisplayMetrics().density;
//		listView.setFirstTopOffset((int) (50 * density));
		listView.setDividerHeight(2);
		listView.setDivider(new ColorDrawable(getResources().getColor(R.color.black)));
		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
//		SimpleHeader header = new SimpleHeader(this);
//		header.setTextColor(0xff0066aa);
//		header.setCircleColor(0xff33bbee);
//		listView.setHeadable(header);

//		// 设置加载更多的样式（可选）
//		SimpleFooter footer = new SimpleFooter(this);
//		footer.setCircleColor(0xff33bbee);
//		listView.setFootable(footer);

		// 设置列表项出现动画（可选）
		listView.setItemAnimForTopIn(R.anim.topitem_in);
		listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

		// 下拉刷新事件回调（可选）
//		listView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
//			@Override
//			public void onStart() {
//				refresh();
//			}
//		});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		setRefresh();
	}
	/*
* 设置下拉刷新
* */
	SwipeRefreshLayout swipeRefreshView;
	private void setRefresh() {
		// 不能在onCreate中设置，这个表示当前是刷新状态，如果一进来就是刷新状态，SwipeRefreshLayout会屏蔽掉下拉事件
		//swipeRefreshLayout.setRefreshing(true);

		// 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
		// 设置下拉进度的背景颜色，默认就是白色的
		swipeRefreshView= (SwipeRefreshLayout) findViewById(R.id.srl);
		swipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
		// 设置下拉进度的主题颜色
		swipeRefreshView.setColorSchemeResources(R.color.edt_hint_orange, R.color.commit_orange);

		// 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
		swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				// 这里是主线程
				// 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
				// TODO 获取数据
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Map<String, String> params = new HashMap<String, String>();
						initData();
						// 加载完数据设置为不刷新状态，将下拉进度收起来
						swipeRefreshView.setRefreshing(false);
					}
				}, 1200);
			}
		});
	}
	public void initData(){
		loadDialog.show();
		listView.hiddenRight(listView.mPreItemView);
		Map<String, String> params = new HashMap<String, String>();
		manager.advertList(params, this);
	}
	@Override
	public void onSuccess(int requstCode, JSONObject result) {
		listView.setRefreshSuccess("加载成功"); // 通知加载成功
		listView.stopLoadMore();
		switch (requstCode) {
			case DadanUrl.USER_LOGIN_AGAIN_REQUEST_CODE:
				if (result.optString("LogionCode").equals("1")) {
					DadanPreference.getInstance(this).setTicket(result.optString("Ticket"));
					initData();
				}else if(result.optString("LogionCode").equals("-1")){
					Intent intent=new Intent(this,LoginActivity.class);
					intent.putExtra("LogionCode","-1");
					startActivity(intent);
					ActivityManager.getInstance().finishAllActivity();
				}
				break;
		case DadanUrl.ADVER_DEL_URL_CODE:{
			initData();
			break;
		}
		default:
			break;
		}
		loadDialog.dismiss();
	}

	@Override
	public void onFaile(int requestCode, int status, String msg) {
		loadDialog.dismiss();
		listView.setRefreshFail("加载失败");
		listView.stopLoadMore();
//		检测ticket过期和账号被别的设备挤掉
		if(requestCode==HttpUtil.ST_ACCOUNT_OTHER_LOGIN_FAILE||requestCode==233){
			Map<String, String> params = new HashMap<String, String>();
			params.put("DEVICE_ID", ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
			manager.loginAgain(params, this);
		}
	}
	@Override
	public void onSuccess(int requstCode, JSONArray result) {
		listView.setRefreshSuccess("加载成功"); // 通知加载成功
		listView.stopLoadMore();
		switch (requstCode) {
		case DadanUrl.ADVER_LIST_URL_CODE:{
			try {
				list.clear();
				adapter.notifyDataSetChanged();
				for(int i=0;i<result.length();i++){
					JSONObject jsonObj = result.getJSONObject(i);
					String id = jsonObj.optString("ID");
					String name = jsonObj.optString("PromotionName");
					String no = jsonObj.optString("PromotionNo");
					String status = jsonObj.optString("PromotionStatus");
					String statusId = jsonObj.optString("PromotionStatusId");
					String QRCodeCount = jsonObj.optString("QRCodeCount");
					String url = jsonObj.optString("PicUrl");
					Advert advert = new Advert();
					advert.setId(id);
					advert.setName(name);
					advert.setNo(no);
					advert.setStatus(status);
					advert.setQRCodeCount(QRCodeCount);
					advert.setStatusId(statusId);
					advert.setUrl(url);
					list.add(advert);
					adapter.notifyDataSetChanged();
					listView.setRefreshSuccess("加载成功"); // 通知加载成功
					listView.stopLoadMore();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				
			}
			break;
		}
		default:
			break;
		}
		loadDialog.dismiss();
	}
	//编辑操作
	public void edit(View v){
		listView.hiddenRight(listView.mPreItemView);
		View parent = (View) v.getParent().getParent();
		TextView indexView = (TextView) parent.findViewById(R.id.id);
		if(indexView!=null){
			int index = Integer.valueOf(indexView.getText().toString());
			if(list.size()>index&&index>=0){
				Advert advert = list.get(index);
				Intent intent = new Intent(this,ActivityAdvertAdd.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("advert", advert);
				intent.putExtras(bundle);
				startActivityForResult(intent,200);
			}else{
				listView.hiddenRight(listView.mPreItemView);
			}
		}
	}
	//删除操作
	public void delete(final View v){
		listView.hiddenRight(listView.mPreItemView);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_tips_layout, null);
		new TipsDialog.Builder(ActivityAdvert.this).setTitle("删除").setMessage("将删除所有与之相关的订单记录，是否确认删除？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadDialog.show();
						View parent = (View) v.getParent().getParent();
						TextView indexView = (TextView) parent.findViewById(R.id.id);
						if(indexView!=null){
							int index = Integer.valueOf(indexView.getText().toString());
							if(list.size()>index){
								Advert item = list.get(index);
								JSONObject params = new JSONObject();
								try {
									params.put("id", item.getId());
									manager.delAdvert(params, ActivityAdvert.this);
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create(layout).show();
	}
	@Override
	public void onClick(View v) {
		listView.hiddenRight(listView.mPreItemView);
		switch (v.getId()) {
		case R.id.btn_add:{
			Intent addIntent = new Intent(this,ActivityAdvertAdd.class);
			startActivityForResult(addIntent,200);
			break;
		}
		case R.id.layout_menu_home:
				this.finish();
				break;
		default:
			break;
		}
	}
	//网络获取图片
	private Bitmap getBitmapFromUrl(String urlString) {
		Bitmap bitmap;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				assert is != null;
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	//图片异步加载类
	     public class ImageSaveTask extends AsyncTask<String, Void, Void> {
			         @Override
		       protected Void doInBackground(String... params) {
						 Bitmap bitmap =getBitmapFromUrl(params[0]);
						 FileUtils.saveImageToGallery(ActivityAdvert.this,bitmap);
						 loadDialog.dismiss();
			            return null;
			         }

				       public void onProgressUpdate(Void... voids) {

			         }
		   }
         //图片异步加载类
	     public class ImageShareTask extends AsyncTask<String, Void, Void> {
			         @Override
		       protected Void doInBackground(String... params) {
					Bitmap bitmap =getBitmapFromUrl(params[0]);
	                FileUtils.saveImageToGallerys(ActivityAdvert.this,bitmap);
	                loadDialog.dismiss();
					showShare();
			            return null;
			         }

				       public void onProgressUpdate(Void... voids) {

			         }
		   }
	private static final int MY_PERMISSIONS_REQUEST = 1;
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (requestCode == MY_PERMISSIONS_REQUEST)
		{
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
			{
				Intent intent = new Intent(ActivityAdvert.this, CaptureActivity.class);
				startActivity(intent);
			} else
			{
				// Permission Denied
				CustomToast.showToast(ActivityAdvert.this,getResources().getString(R.string.permission_tip));
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	//获取二维码布局
	public void qrcode(View v){
		listView.hiddenRight(listView.mPreItemView);
		View parent = (View) v.getParent().getParent();
		TextView indexView = (TextView) parent.findViewById(R.id.id);
		if(indexView!=null){
			int index = Integer.valueOf(indexView.getText().toString());
			if(list.size()>index){
				getQrcode(index);
			}
		}
	}
	private View popupView;
	private PopupWindow window;
	private Bitmap share_bitmap;
	private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
	/**
	 * 加载popupwindow
	 */
	private void getQrcode(final int index) {
		popupView = getLayoutInflater().inflate(R.layout.qrcode_layout_popwindow, null);
		window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		//设置动画
		window.setAnimationStyle(R.style.popup_window_anim);
		// 设置背景颜色
		window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
		//设置可以获取焦点
		window.setFocusable(true);
		//设置可以触摸弹出框以外的区域
		window.setOutsideTouchable(true);
		// 更新popupwindow的状态
		window.update();
		backgroundAlpha(0.6f);
		//添加pop窗口关闭事件
		window.setOnDismissListener(new ActivityAdvert.poponDismissListener());
		// 以下拉的方式显示，并且可以设置显示的位置
		window.showAtLocation(findViewById(R.id.choose_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		final ImageView img_desk= (ImageView) popupView.findViewById(R.id.img_desk);
		ImageView img_cancel= (ImageView) popupView.findViewById(R.id.img_cancel);
		final LinearLayout save_lay= (LinearLayout) popupView.findViewById(R.id.save_lay);
		LinearLayout share_lay= (LinearLayout) popupView.findViewById(R.id.share_lay);
		img_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		save_lay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (ContextCompat.checkSelfPermission(ActivityAdvert.this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED)
				{
					ActivityCompat.requestPermissions(ActivityAdvert.this,
							new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
							MY_PERMISSIONS_REQUEST_CALL_PHONE2);
				}else {
					loadDialog.show();
					new ImageSaveTask().execute(list.get(index).getUrl());
				}

			}
		});
		share_lay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ContextCompat.checkSelfPermission(ActivityAdvert.this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED)
				{
					ActivityCompat.requestPermissions(ActivityAdvert.this,
							new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
							MY_PERMISSIONS_REQUEST_CALL_PHONE2);
				}else {
					loadDialog.show();
					new ImageShareTask().execute(list.get(index).getUrl());
				}

			}
		});
		if (list.get(index).getUrl()==null||list.get(index).getUrl().equals("")) {
			GlideUtils.displayNative(img_desk, R.mipmap.qrcode_default);
		} else {
			GlideUtils.display(img_desk,list.get(index).getUrl());
		}
		TextView tv_name= (TextView) popupView.findViewById(R.id.tv_name);
		Gson gson = new Gson();
		StoreMessage storeMessage=gson.fromJson(DadanPreference.getInstance(ActivityAdvert.this).getString("StoreMessage"),StoreMessage.class);
		tv_name.setText(storeMessage.getCompanyName());
		TextView tv_address= (TextView) popupView.findViewById(R.id.tv_address);
		tv_address.setText("地址："+storeMessage.getProvinceName()+"省"+storeMessage.getCityName()+"市"+storeMessage.getAreaName()+"区"+storeMessage.getCompanyAddress());
		TextView tv_phone= (TextView) popupView.findViewById(R.id.tv_phone);
		tv_phone.setText("电话："+storeMessage.getTelePhone());
		TextView tv_desk_name= (TextView) popupView.findViewById(R.id.tv_desk_name);
		tv_desk_name.setText(list.get(index).getName());
		TextView tv_desk_no= (TextView) popupView.findViewById(R.id.tv_desk_no);
		tv_desk_no.setText(list.get(index).getNo());
		share_bitmap=img_desk.getDrawingCache();
	}
	//分享操作
	private void showShare() {
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权  
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用  
		oks.setTitle(getResources().getString(R.string.app_name));
		// titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用  
//		oks.setTitleUrl("https://www.baidu.com/");
		// text是分享文本，所有平台都需要这个字段  
//		oks.setText("我是分享文本");
		//分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博  
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");  
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数  
		oks.setImagePath(Environment.getExternalStorageDirectory()+ "/Food/share.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用  
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			@Override
			public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
				if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
					paramsToShare.setText(null);
					paramsToShare.setTitle(null);
					paramsToShare.setTitleUrl(null);
					paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+ "/Food/share.jpg");
				} else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
					paramsToShare.setText(null);
					paramsToShare.setTitle(null);
					paramsToShare.setTitleUrl(null);
					paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+ "/Food/share.jpg");
				}else if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
					paramsToShare.setText(null);
					paramsToShare.setTitle(null);
					paramsToShare.setTitleUrl(null);
					paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+ "/Food/share.jpg");
				}else if (platform.getName().equalsIgnoreCase(WechatMoments.NAME)) {
					paramsToShare.setText(null);
					paramsToShare.setTitle(null);
					paramsToShare.setTitleUrl(null);
					paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+ "/Food/share.jpg");
				} else if (platform.getName().equalsIgnoreCase(SinaWeibo.NAME)) {
					paramsToShare.setText(null);
					paramsToShare.setTitle(null);
					paramsToShare.setTitleUrl(null);
					paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+ "/Food/share.jpg");
				}
			}
		});
//		oks.setUrl("https://www.baidu.com/");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用  
//		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用  
//		oks.setSite("test");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用  
//		oks.setSiteUrl("https://www.baidu.com/");
// 启动分享GUI
		oks.show(this);
	}
	/**
	 * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
	 * @author cg
	 *
	 */
	class poponDismissListener implements PopupWindow.OnDismissListener{
		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			backgroundAlpha(1f);
		}
	}
	/**
	 * 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha)
	{
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		getWindow().setAttributes(lp);
	}
	/**
	 * 点击底部菜单事件
	 * @param v
	 */
	public void clickMenu(View v){
		Intent intent=new Intent(this,MainActivity.class);
		intent.putExtra("isOrder","order");
		startActivity(intent);
		ActivityManager.getInstance().finishAllActivity();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		initData();
	}
	@Override
	public void onItemClick(ZrcListView parent, View view, int position, long id) {
		Advert advert = list.get(position);
		Intent intent = new Intent(this,ActivityAdvertAdd.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("advert", advert);
		intent.putExtras(bundle);
		startActivityForResult(intent,200);
	}
}
