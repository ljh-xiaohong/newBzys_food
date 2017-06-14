package com.bapm.bzys.newBzys_food.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.bapm.bzys.newBzys_food.base.UseDeviceSizeApplication;
import com.bapm.bzys.newBzys_food.buletooth.PrintUtils;
import com.bapm.bzys.newBzys_food.buletooth.contants.BltContant;
import com.bapm.bzys.newBzys_food.buletooth.manager.BltManager;
import com.bapm.bzys.newBzys_food.util.CustomToast;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullListView;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullListViewAdapter;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullViewHolder;
import com.bapm.bzys.newBzys_food.widget.dialog.TipsDialog;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityConnectBuleTooth extends BaseActivity {

    @BindView(R.id.connceted_recycler)
    NestFullListView conncetedRecycler;
    @BindView(R.id.wait_conncet_recycler)
    NestFullListView waitConncetRecycler;
    int i = 0;
    @BindView(R.id.layout_title_bar)
    AutoRelativeLayout layoutTitleBar;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.lay_search)
    AutoLinearLayout laySearch;
    private boolean isSuccess;
    private List<BluetoothDevice> bltList=new ArrayList<BluetoothDevice>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://搜索蓝牙
//                    btlBar.setVisibility(View.GONE);
                    loadDialog.dismiss();
                    BltManager.getInstance().clickBlt(ActivityConnectBuleTooth.this, BltContant.BLUE_TOOTH_SEARTH_CENCLE);
                    break;
                case 2:
                    loadDialog.dismiss();
                    CustomToast.showToast(ActivityConnectBuleTooth.this, "连接失败");
                    break;
                case 3:
                    break;
                case 4://已连接某个设备
                    loadDialog.dismiss();
                    BluetoothDevice device1 = (BluetoothDevice) msg.obj;
                    DadanPreference.getInstance(ActivityConnectBuleTooth.this).setString("address", device1.getAddress());
                    getBondedDevices();
                    if (!bondedDevicesList.contains(device1)) {
                        bondedDevicesList.add(device1);
                    }
                    if (i != 0) {
                        conncetedRecycler.removedCahce();
                    }
                    conncetedRecycler.setAdapter(new NestFullListViewAdapter<BluetoothDevice>(R.layout.activity_buletooth_devices_item, bondedDevicesList) {
                        @Override
                        public void onBind(int pos, BluetoothDevice bondedDevices, NestFullViewHolder holder) {
                            setData(holder, bondedDevices, "bonded");
                            i=1;
                        }
                    });
                    if (i != 0) {
                        waitConncetRecycler.removedCahce();
                    }
                    if (bltList.size()!=0) {
                        bltList.remove(bltList.indexOf(device1));
                        waitConncetRecycler.setAdapter(new NestFullListViewAdapter<BluetoothDevice>(R.layout.activity_buletooth_devices_item, bltList) {
                            @Override
                            public void onBind(int pos, BluetoothDevice unbondDevices, NestFullViewHolder holder) {
                                setData(holder, unbondDevices, "unbond");
                            }
                        });
                    }
                    if (UseDeviceSizeApplication.bluetoothSocket != null) {
                        try {
                            isSuccess = true;
                            PrintUtils.setOutputStream(UseDeviceSizeApplication.bluetoothSocket.getOutputStream());
                            CustomToast.showToast(ActivityConnectBuleTooth.this, "连接成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 5:
                    Toast.makeText(ActivityConnectBuleTooth.this, "先取消与打印机的配对，再回到APP连接打印机进行打印", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
            }
        }
    };
    private List<BluetoothDevice> bondedDevicesList = new ArrayList<BluetoothDevice>();

    /*
    *  获取所有已经绑定的蓝牙设备
    */
    private void getBondedDevices() {
        bondedDevicesList.clear();
        Set<BluetoothDevice> devices = BltManager.getInstance().getmBluetoothAdapter().getBondedDevices();
        bondedDevicesList.addAll(devices);
        if (i != 0) {
            conncetedRecycler.removedCahce();
        }
        for (int i=0;i<bondedDevicesList.size();i++){
            if (!bondedDevicesList.get(i).getAddress().equals(DadanPreference.getInstance(ActivityConnectBuleTooth.this).getString("address"))){
                bondedDevicesList.remove(i);
            }
        }
        conncetedRecycler.setAdapter(new NestFullListViewAdapter<BluetoothDevice>(R.layout.activity_buletooth_devices_item, bondedDevicesList) {
            @Override
            public void onBind(int pos, BluetoothDevice bondedDevices, NestFullViewHolder holder) {
                setData(holder, bondedDevices,"bonded");
                i=1;
            }
        });
    }

    private void setData(NestFullViewHolder holder, final BluetoothDevice bondedDevices, final String what) {
        holder.setText(R.id.tv_devices_name, bondedDevices.getName());
        holder.setOnClickListener(R.id.tv_devices_name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (what.equals("unbond")) {
                    if (bondedDevicesList.size()==0) {
                        BltManager.getInstance().clickBlt(ActivityConnectBuleTooth.this, BltContant.BLUE_TOOTH_SEARTH_CENCLE);
                        final BluetoothDevice bluetoothDevice = bltList.get(bltList.indexOf(bondedDevices));
                        //链接的操作应该在子线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                BltManager.getInstance().connect(bluetoothDevice, handler);
                            }
                        }).start();
                    }else{
//                        Toast.makeText(ActivityConnectBuleTooth.this, "只能连接一台打印机，如需更换请先取消与打印机的配对，再回到APP连接打印机进行打印", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent();
//                        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        LayoutInflater inflater = (LayoutInflater)ActivityConnectBuleTooth.this
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.dialog_tips_layout, null);
                        new TipsDialog.Builder(ActivityConnectBuleTooth.this).setTitle("提示").setMessage("是否确更换打印机连接，请核准后点击确认。")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        BltManager.getInstance().clickBlt(ActivityConnectBuleTooth.this, BltContant.BLUE_TOOTH_SEARTH_CENCLE);
                                        final BluetoothDevice bluetoothDevice = bltList.get(bltList.indexOf(bondedDevices));
                                        DadanPreference.getInstance(ActivityConnectBuleTooth.this).setString("address", bluetoothDevice.getAddress());
                                        //链接的操作应该在子线程
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                BltManager.getInstance().connect(bluetoothDevice, handler);
                                            }
                                        }).start();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create(layout).show();
                    }
                }else{
                    Intent intent=new Intent(ActivityConnectBuleTooth.this,ActivitySettingPrint.class);
                    startActivity(intent);
//                    final BluetoothDevice bluetoothDevice = bondedDevicesList.get(bondedDevicesList.indexOf(bondedDevices));
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            BltManager.getInstance().connect(bluetoothDevice, handler);
//                        }
//                    }).start();
//                    if (PrintUtils.getOutputStream()==null||!isSuccess){
//                        Toast.makeText(ActivityConnectBuleTooth.this,"请先连接打印机",Toast.LENGTH_LONG).show();
//                    }else{
//                        print();
//                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_buletooth);
        ButterKnife.bind(this);
        BltManager.getInstance().initBltManager(this);
        //检查蓝牙是否开启
        BltManager.getInstance().checkBleDevice(this);
        initData();
    }

    private void initData() {
//        blue_list.setOnItemClickListener(this);
//        blue_list.setAdapter(myAdapter);
        //注册蓝牙扫描广播
        loadDialog.show();
        blueToothRegister();
        //更新蓝牙开关状态
//        checkBlueTooth();
        //第一次进来搜索设备
//        BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
        getBondedDevices();
        bltList.clear();
        BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
        handler.sendEmptyMessageDelayed(1,20000);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                BltManager.getInstance().getBltList(handler);
//            }
//        }).start();

//        handler.sendEmptyMessageDelayed(1, 20000);
    }

    /**
     * 注册蓝牙回调广播
     */
    private void blueToothRegister() {
        BltManager.getInstance().registerBltReceiver(this, new BltManager.OnRegisterBltReceiver() {

            /**搜索到新设备
             * @param device
             */
            @Override
            public void onBluetoothDevice(BluetoothDevice device) {
//                Log.e("device",device.getName());
//                && device.getBluetoothClass().getMajorDeviceClass() == 1536
                if (device.getBluetoothClass().getMajorDeviceClass()==1536&&!bltList.contains(device)&&!bondedDevicesList.contains(device)) {
                    bltList.add(device);
                    waitConncetRecycler.setAdapter(new NestFullListViewAdapter<BluetoothDevice>(R.layout.activity_buletooth_devices_item, bltList) {
                        @Override
                        public void onBind(int pos, BluetoothDevice bondedDevices, NestFullViewHolder holder) {
                            setData(holder, bondedDevices, "unbond");
                        }
                    });
//                    }
                }
            }

            /**连接中
             * @param device
             */
            @Override
            public void onBltIng(BluetoothDevice device) {
            }

            /**连接完成
             * @param device
             */
            @Override
            public void onBltEnd(BluetoothDevice device) {
            }

            /**取消链接
             * @param device
             */
            @Override
            public void onBltNone(BluetoothDevice device) {
                if (bondedDevicesList.size()!=0) {
                    bondedDevicesList.remove(bondedDevicesList.contains(device));
                    DadanPreference.getInstance(ActivityConnectBuleTooth.this).setString("address","");
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //页面关闭的时候要断开蓝牙
        BltManager.getInstance().unregisterReceiver(this);
    }

    @OnClick(R.id.lay_search)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.lay_search:
//                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                        this, R.anim.loading_animation);
//                // 使用ImageView显示动画
//                img_load.startAnimation(hyperspaceJumpAnimation);
                loadDialog.show();
                bltList.clear();
                BltManager.getInstance().clickBlt(this, BltContant.BLUE_TOOTH_SEARTH);
                handler.sendEmptyMessageDelayed(1,20000);
                break;
        }
    }
}
