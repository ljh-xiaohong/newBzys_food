package com.bapm.bzys.newBzys_food.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * webview 工具类
 * 
 * @author 小洪
 * 
 */
public class LoadWebUtils {
	private WebView webView;
	private Activity context;
	private String string;

	public LoadWebUtils(WebView webView, Activity context, String string) {
		super();
		this.webView = webView;
		this.context = context;
		this.string = string;
	}

	public void loadWeb() {
		setws();
//		if (context.getLocalClassName().equals("activity.FeedBackActivity")) {
		Map<String,String> params=new HashMap<String,String>();
		Log.e("ticket",DadanPreference.getInstance(context).getTicket());
		webView.loadUrl(string+"?ticket="+DadanPreference.getInstance(context).getTicket());
//		} else {
//			String string="http://gdeciq.gsstyun.com/gdeciq/public/zh/getOrder?"+"{\"real_code\":\"1601030000037250\",\"query_type\":\"40\","
//					+ "\"enterprise_code\":\"100\",\"access_token\":\"100\"}";
//			String[] str=string.split("[?]");
//			Log.e("asd",str[0]+"     "+str[1]);
//			String json = "{\"real_code\":\"1601030000037250\",\"query_type\":\"40\","
//					+ "\"enterprise_code\":\"100\",\"access_token\":\"100\"}";
//			webView.postUrl(
//					str[0],
//					str[1].getBytes());
//		}
		webView.addJavascriptInterface(new JsFunction(), "androidJS");
		// webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if (context == null) {
					return false;
				}
				// 判断url类型
				if (url.startsWith("mailto:") || url.startsWith("geo:")
						|| url.startsWith("tel:") || url.startsWith("smsto:")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					context.startActivity(intent);
					return true;
				}
				return false;
			}
		});
		// 下载监听
//		webView.setDownloadListener(new DownloadListener() {
//			@Override
//			public void onDownloadStart(String url, String userAgent,
//					String contentDisposition, String mimetype,
//					long contentLength) {
//				DownloaderTask task = new DownloaderTask(context);
//				task.execute(url);
//			}
//		});
	}

	private void setws() {
		// TODO Auto-generated method stub
		WebSettings ws = webView.getSettings();
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		ws.setJavaScriptEnabled(true); 
		ws.setUseWideViewPort(true);
		ws.setLoadWithOverviewMode(true);
		ws.setSaveFormData(true);
		ws.setAppCacheEnabled(true);
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setCacheMode(ws.LOAD_NO_CACHE);
		ws.setGeolocationEnabled(true);
		ws.setDomStorageEnabled(true);
		ws.setDefaultTextEncodingName("utf-8");
		ws.setDisplayZoomControls(false);
		ws.setAllowFileAccess(true); 
		ws.setBuiltInZoomControls(false); 
		ws.setSupportZoom(true); 
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 240) {
			ws.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == 160) {
			ws.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 120) {
			ws.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
			ws.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == DisplayMetrics.DENSITY_TV) {
			ws.setDefaultZoom(ZoomDensity.FAR);
		} else {
			ws.setDefaultZoom(ZoomDensity.MEDIUM);
		}
	}

	final class JsFunction {
		@JavascriptInterface
		public void call(String call) {
			Intent intent = new Intent(Intent.ACTION_DIAL);
			Uri data = Uri.parse("tel:" + call);
			intent.setData(data);
			context.startActivity(intent);
		}

		@JavascriptInterface
		public void send_email(String email) {
			Intent intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("mailto: " + email));
			context.startActivity(intent);
		}
	}
}
