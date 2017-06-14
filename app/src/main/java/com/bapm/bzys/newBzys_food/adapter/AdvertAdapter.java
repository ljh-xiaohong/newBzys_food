package com.bapm.bzys.newBzys_food.adapter;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.model.Advert;
import com.bapm.bzys.newBzys_food.util.GlideUtils;
import com.bapm.bzys.newBzys_food.widget.ZrcListView;
import com.bapm.bzys.newBzys_food.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import okhttp3.Call;

public class AdvertAdapter extends BaseAdapter {

	// 定义Context
	private LayoutInflater inflater;
	private List<Advert> list;
	private Activity context;

	public AdvertAdapter(Activity context, List<Advert> list) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_advert_list_item, null, false);
			holder = new ViewHolder();
			holder.item_left = (RelativeLayout) convertView.findViewById(R.id.item_left);
			holder.item_right = (LinearLayout) convertView.findViewById(R.id.item_right);
			holder.id = (TextView) convertView.findViewById(R.id.id);
			holder.tv_no = (TextView) convertView.findViewById(R.id.tv_no);
			holder.tv_code_count = (TextView) convertView.findViewById(R.id.tv_code_count);
			holder.arrow = (TextView) convertView.findViewById(R.id.arrow);
			holder.iv_qrcode = (ImageView) convertView.findViewById(R.id.iv_qrcode);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.tvRightItem = (TextView) convertView.findViewById(R.id.tvRightItem);
			holder.tvEdtRightItem = (TextView) convertView.findViewById(R.id.tvEdtRightItem);
			holder.btn_pause= (Button)convertView.findViewById(R.id.btn_pause);
			convertView.setTag(holder);
		} else {// 有直接获得ViewHolder
			holder = (ViewHolder) convertView.getTag();
		}
		LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		holder.item_left.setLayoutParams(lp1);
		DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
		int rightWidth =(int)(context.getResources().getDimensionPixelSize(R.dimen.activity_goods_type_operate_unit_width)*displayMetrics.densityDpi/320);
		((ZrcListView)parent).setItemRightWidths(position, rightWidth*2);
		if(parent instanceof ZrcListView){
			LayoutParams lp2 = new LayoutParams(((ZrcListView)parent).getRightViewWidth(position), LayoutParams.MATCH_PARENT);
			holder.item_right.setLayoutParams(lp2);
		}
		holder.id.setText(String.valueOf(position));
		holder.tv_code_count.setText("已添加二维码"+list.get(position).getQRCodeCount()+"个");
		holder.name.setText(list.get(position).getName());
		holder.iv_qrcode.setImageResource(R.mipmap.qrcode_default);
		holder.tv_no.setText("/"+list.get(position).getNo());
		if (list.get(position).getUrl()==null||list.get(position).getUrl().equals("")) {
			GlideUtils.displayNative(holder.iv_qrcode, R.mipmap.qrcode_default);
		} else {
			OkHttpUtils
					.get()//
					.url(list.get(position).getUrl())//
					.build()//
					.execute(new BitmapCallback()
					{
						@Override
						public void onError(Call call, Exception e, int id) {
							GlideUtils.displayNative(holder.iv_qrcode, R.mipmap.img_fairl);
						}
						@Override
						public void onResponse(Bitmap response, int id) {
							holder.iv_qrcode.setImageBitmap(response);
						}
					});
		}
		if(list.get(position).getStatus().contains("暂停")){
			holder.arrow.setEnabled(false);
			holder.arrow.setBackgroundResource(R.drawable.btn_receipt_gray_bg);
			holder.arrow.setTextColor(context.getResources().getColor(R.color.black6));
			holder.btn_pause.setVisibility(View.VISIBLE);
			holder.iv_qrcode.setVisibility(View.GONE);
		}else{
			holder.arrow.setEnabled(true);
			holder.arrow.setBackgroundResource(R.drawable.btn_receipt_normal_bg);
			holder.arrow.setTextColor(context.getResources().getColor(R.color.edt_hint_orange));
			holder.btn_pause.setVisibility(View.GONE);
			holder.iv_qrcode.setVisibility(View.VISIBLE);
		}
		holder.arrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ContextCompat.checkSelfPermission(context,
						Manifest.permission.CAMERA)
						!= PackageManager.PERMISSION_GRANTED)
				{
					ActivityCompat.requestPermissions(context,
							new String[]{Manifest.permission.CAMERA},
							MY_PERMISSIONS_REQUEST);
				}else {
					Intent intent = new Intent(context, CaptureActivity.class);
					intent.putExtra("promotionId",list.get(position).getId());
					context.startActivity(intent);
				}
			}
		});
		return convertView;
	}
	private static final int MY_PERMISSIONS_REQUEST = 1;
	private static class ViewHolder {
		RelativeLayout item_left;
		LinearLayout item_right;
		TextView id,name,number,tvRightItem,tv_no,tvEdtRightItem,tv_code_count,arrow;
		ImageView iv_qrcode;
		Button btn_pause;
	}
}