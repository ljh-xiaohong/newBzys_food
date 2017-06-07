package com.bapm.bzys.newBzys_food.util;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class AsyncImageLoader {
	//保存正在下载的图片URL集合，避免重复下载用
	private static HashSet<String> sDownloadingSet;
	//软引用内存缓存
	private static Map<String,SoftReference<Bitmap>> sImageCache; 
	//图片三种获取方式管理者，网络URL获取、内存缓存获取、外部文件缓存获取
	private static LoaderImpl impl;
	//线程池相关
	private static ExecutorService sExecutorService;
	
	//通知UI线程图片获取ok时使用
	private Handler handler; 
	
	private static AsyncImageLoader imageLoader;
	public static AsyncImageLoader getInstance(Context context){
		if(imageLoader==null){
			imageLoader = new AsyncImageLoader(context);
			//将图片缓存至外部文件中  
			imageLoader.setCache2File(true); //false  
	        //设置外部缓存文件夹  
			imageLoader.setCachedDir(context.getCacheDir().getAbsolutePath()+"/image");
			if(!new File(context.getCacheDir().getAbsolutePath()+"/image").exists()){
				new File(context.getCacheDir().getAbsolutePath()+"/image").mkdir();
			}
		}
		return imageLoader;
	}
	/**
	 * 异步加载图片完毕的回调接口
	 */
	public interface ImageCallback{
		/**
		 * 回调函数
		 * @param bitmap: may be null!
		 * @param imageUrl 
		 */
		public void onImageLoaded(ImageView imageView, Bitmap bitmap, String imageUrl);
	}
	
	static{
		sDownloadingSet = new HashSet<String>();
		sImageCache = new HashMap<String,SoftReference<Bitmap>>();
		impl = new LoaderImpl(sImageCache);
	}
	private AsyncImageLoader(Context context){
		handler = new Handler();
		startThreadPoolIfNecessary();
		
		String defaultDir = context.getCacheDir().getAbsolutePath();
		setCachedDir(defaultDir);
	}
	
	/**
	 * 是否缓存图片至/data/data/package/cache/目录
	 * 默认不缓存
	 */
	public void setCache2File(boolean flag){
		impl.setCache2File(flag);
	}
	
	/**
	 * 设置缓存路径，setCache2File(true)时有效
	 */
	public void setCachedDir(String dir){
		impl.setCachedDir(dir);
	}

	/**开启线程池*/
	public static void startThreadPoolIfNecessary(){
		if(sExecutorService == null || sExecutorService.isShutdown() || sExecutorService.isTerminated()){
			sExecutorService = Executors.newFixedThreadPool(3);
			//sExecutorService = Executors.newSingleThreadExecutor();
		}
	}
	
	/**
	 * 异步下载图片，并缓存到memory中
	 * @param url	
	 * @param callback	see ImageCallback interface
	 */
	public void downloadImage(final String url,ImageView imageView,final ImageCallback callback){
		downloadImage(url,imageView, true, callback);
	}
	
	/**
	 * 
	 * @param url
	 * @param cache2Memory 是否缓存至memory中
	 * @param callback
	 */
	public void downloadImage(final String url,final ImageView imageView, final boolean cache2Memory, final ImageCallback callback){
		if(sDownloadingSet.contains(url)){
			Log.i("AsyncImageLoader", "###该图片正在下载，不能重复下载！");
			return;
		}
		
		Bitmap bitmap = impl.getBitmapFromMemory(url);
		if(bitmap != null){
			if(callback != null){
				callback.onImageLoaded(imageView,bitmap, url);
			}
		}else{
			//从网络端下载图片
			sDownloadingSet.add(url);
			sExecutorService.submit(new Runnable(){
				@Override
				public void run() {
					final Bitmap bitmap = impl.getBitmapFromUrl(url, cache2Memory);
					handler.post(new Runnable(){
						@Override
						public void run(){
							if(callback != null)
								callback.onImageLoaded(imageView,bitmap, url);
							sDownloadingSet.remove(url);
						}
					});
				}
			});
		}
	}
	
	/**
	 * 预加载下一张图片，缓存至memory中
	 * @param url 
	 */
	public void preLoadNextImage(final String url,final ImageView imageView){
		//将callback置为空，只将bitmap缓存到memory即可。
		downloadImage(url,imageView, null);
	}
	
}