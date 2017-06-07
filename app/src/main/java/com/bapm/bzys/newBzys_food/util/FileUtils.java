package com.bapm.bzys.newBzys_food.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bapm.bzys.newBzys_food.activity.ActivityAdvert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fs-ljh on 2017/5/26.
 */

public class FileUtils {
    private static File appDir;

    // 保存图片到本地food文件夹，并且把文件插入到系统图库（某些系统不兼容，如小米系统）
    public static void saveImageToGallery(Context context,   Bitmap bitmap) {
//        if (){
//            appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        }
         appDir = new File(Environment.getExternalStorageDirectory(), "Food");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
//        Log.e("path",file.getPath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        file.getAbsolutePath(), fileName, null);
                // 重新扫描图库
                MediaScannerConnection.scanFile(context,new String[] { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()+ "/"+ file.getParentFile().getAbsolutePath() }, null,null);
                CustomToast.showToast(context,"保存成功");
            } catch (FileNotFoundException e) {
                CustomToast.showToast(context,"保存失败");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            CustomToast.showToast(context,"保存失败");
            e.printStackTrace();
        } catch (IOException e) {
            CustomToast.showToast(context,"保存失败");
            e.printStackTrace();
        }
    }
    //分享图片保存到本地
    public static void saveImageToGallerys(Context context,   Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Food");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName =  "share.jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}