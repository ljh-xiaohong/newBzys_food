package com.bapm.bzys.newBzys_food.zxing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.activity.ActivityAdvert;
import com.bapm.bzys.newBzys_food.activity.FragmentOrder;
import com.bapm.bzys.newBzys_food.activity.FristActivity;
import com.bapm.bzys.newBzys_food.activity.LoginActivity;
import com.bapm.bzys.newBzys_food.activity.MainActivity;
import com.bapm.bzys.newBzys_food.base.MPermissionsActivity;
import com.bapm.bzys.newBzys_food.model.StoreMessage;
import com.bapm.bzys.newBzys_food.network.DadanUrl;
import com.bapm.bzys.newBzys_food.network.HttpUtil;
import com.bapm.bzys.newBzys_food.network.function.interf.Function;
import com.bapm.bzys.newBzys_food.network.function.interf.FunctionManager;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.bapm.bzys.newBzys_food.util.CustomToast;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.util.GlideUtils;
import com.bapm.bzys.newBzys_food.widget.dialog.TipsDialog;
import com.bapm.bzys.newBzys_food.zxing.camera.CameraManager;
import com.bapm.bzys.newBzys_food.zxing.decoding.CaptureActivityHandler;
import com.bapm.bzys.newBzys_food.zxing.decoding.InactivityTimer;
import com.bapm.bzys.newBzys_food.zxing.decoding.RGBLuminanceSource;
import com.bapm.bzys.newBzys_food.zxing.view.ViewfinderView;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends MPermissionsActivity implements Callback , Function {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    //	private Button cancelScanButton;
    public static final int RESULT_CODE_QR_SCAN = 0xA1;
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    private FunctionManager manager;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        manager = this.init(this.getContext());
        manager.registFunClass(CaptureActivity.class);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        //界面上使用
        try {
            checkCameraPermissions();
        } catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CaptureActivity.this);
            builder.setTitle("照相权限被禁止，无法使用该功能!");
            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CaptureActivity.this.finish();
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();

        }
        //添加toolbar
//        addToolbar();
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }
    /**
     * 检查相机权限，如果不能打开相机则抛出异常
     */
    public static void checkCameraPermissions() throws IOException {
        try {
            Camera camera = Camera.open();
            if (camera != null) {
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            throw new IOException();
        }
    }
    private void addToolbar() {
//        ImageView more = (ImageView) findViewById(R.id.scanner_toolbar_more);
//        assert more != null;
//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0003:
                break;
            default:
                CustomToast.showToast(CaptureActivity.this,getResources().getString(R.string.permission_tip));
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.scanner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.scan_local:
//                //打开手机中的相册
//                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
//                innerIntent.setType("image/*");
//                Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
//                this.startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    //获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();

                    mProgress = new ProgressDialog(CaptureActivity.this);
                    mProgress.setMessage("正在扫描...");
                    mProgress.setCancelable(false);
                    mProgress.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = scanningImage(photo_path);
                            if (result != null) {
//                                Message m = handler.obtainMessage();
//                                m.what = R.id.decode_succeeded;
//                                m.obj = result.getText();
//                                handler.sendMessage(m);
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString(INTENT_EXTRA_KEY_QR_SCAN ,result.getText());
//                                Logger.d("saomiao",result.getText());
//                                bundle.putParcelable("bitmap",result.get);
                                resultIntent.putExtras(bundle);
                                CaptureActivity.this.setResult(RESULT_CODE_QR_SCAN, resultIntent);

                            } else {
                                Message m = handler.obtainMessage();
                                m.what = R.id.decode_failed;
                                m.obj = "Scan failed!";
                                handler.sendMessage(m);
                            }
                        }
                    }).start();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 扫描二维码图片的方法
     * @param path
     * @return
     */
    public Result scanningImage(String path) {
        if(TextUtils.isEmpty(path)){
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

        //quit the scan view
//		cancelScanButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				CaptureActivity.this.finish();
//			}
//		});
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        manager.unregistFunctionClass(CaptureActivity.class);
        super.onDestroy();
    }
    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, resultString);
//            System.out.println("sssssssssssssssss scan 0 = " + resultString);
            // 不能使用Intent传递大于40kb的bitmap，可以使用一个单例对象存储这个bitmap
//            bundle.putParcelable("bitmap", barcode);
//            Logger.d("saomiao",resultString);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_CODE_QR_SCAN, resultIntent);
            if (resultString.contains("fsAppByCatering")){
                initPopupwindow(resultString);
            }else{
                LayoutInflater inflater = (LayoutInflater) CaptureActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dialog_tips_layout, null);
                new TipsDialog.Builder(CaptureActivity.this).setTitle("扫码失败！").setMessage("可能二维码失效或网络问题，请检查二维码或网络问题！")
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Message m = handler.obtainMessage();
                                m.what = R.id.restart_preview;
                                m.obj = "Scan again!";
                                handler.sendMessage(m);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadDialog.dismiss();
                        dialog.dismiss();
                        CaptureActivity.this.finish();
                    }
                }).create(layout).show();
            }


        }
//        CaptureActivity.this.finish();
    }
    private View popupView;
    private PopupWindow window;
    /**
     * 动态生成显示items中的textview
     */
    private void initPopupwindow(final String resultString) {
        popupView = getLayoutInflater().inflate(R.layout.qrcode_sure_layout_popwindow, null);
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
        window.setOnDismissListener(new CaptureActivity.poponDismissListener());
        // 以下拉的方式显示，并且可以设置显示的位置
        window.showAtLocation(findViewById(R.id.choose_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

        TextView img_cancel= (TextView) popupView.findViewById(R.id.btn_cencel);
        TextView img_bind= (TextView) popupView.findViewById(R.id.btn_bind);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                loadDialog.dismiss();
                Message m = handler.obtainMessage();
                m.what = R.id.restart_preview;
                m.obj = "Scan again!";
                handler.sendMessage(m);
            }
        });
        img_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog.show();
                JSONObject prams=new JSONObject();
                try {
                    prams.put("content",resultString);
                    prams.put("promotionId",getIntent().getStringExtra("promotionId"));
                    manager.BindCode(prams,CaptureActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public FunctionManager init(Context context) {
        return new FunctionManager(context);
    }

    @Override
    public void onSuccess(int requstCode, JSONObject result) {
switch (requstCode){
    case DadanUrl.BINDCODE_CODE:
        window.dismiss();
        if (result.optString("Code").equals("1")){
            initStatuePopupwindow(true);
        }else{
            initStatuePopupwindow(false);
        }

        CustomToast.showToast(CaptureActivity.this,result.optString("Message"));
        break;
    case DadanUrl.USER_LOGIN_AGAIN_REQUEST_CODE:
        if (result.optString("LogionCode").equals("1")) {
            DadanPreference.getInstance(this).setTicket(result.optString("Ticket"));
            Message m = handler.obtainMessage();
            m.what = R.id.restart_preview;
            m.obj = "Scan again!";
            handler.sendMessage(m);
        }else if(result.optString("LogionCode").equals("-1")){
            Intent intent=new Intent(this,LoginActivity.class);
            intent.putExtra("LogionCode","-1");
            startActivity(intent);
            ActivityManager.getInstance().finishAllActivity();
        }
        break;
}
        loadDialog.dismiss();
    }
    private View statue_popupView;
    private PopupWindow statue_window;
    private void initStatuePopupwindow(boolean isSuccess) {
        if (isSuccess) {
            statue_popupView = getLayoutInflater().inflate(R.layout.qrcode_bind_success_layout_popwindow, null);
        }else {
            statue_popupView = getLayoutInflater().inflate(R.layout.qrcode_bind_faril_layout_popwindow, null);
        }
        statue_window = new PopupWindow(statue_popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画
        statue_window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        statue_window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        //设置可以获取焦点
        statue_window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        statue_window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        statue_window.update();
//        backgroundAlpha(0.6f);
        //添加pop窗口关闭事件
//        statue_window.setOnDismissListener(new CaptureActivity.poponDismissListener());
        // 以下拉的方式显示，并且可以设置显示的位置
        statue_window.showAtLocation(findViewById(R.id.choose_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        handlers.sendEmptyMessageDelayed(0,3000);//设置3秒后加载ViewPager
        Message m = handler.obtainMessage();
        m.what = R.id.restart_preview;
        m.obj = "Scan again!";
        handler.sendMessage(m);
    }
    private Handler handlers = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0) {
                statue_window.dismiss();
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void onSuccess(int requstCode, JSONArray result) {

    }

    @Override
    public void onFaile(int requestCode, int status, String msg) {
        loadDialog.dismiss();
        if(requestCode== HttpUtil.ST_ACCOUNT_OTHER_LOGIN_FAILE||requestCode==233){
            Map<String, String> params = new HashMap<String, String>();
            params.put("DEVICE_ID", ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
            manager.loginAgain(params, this);
        }
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

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    public void back(View v) {
        this.finish();
    }
}