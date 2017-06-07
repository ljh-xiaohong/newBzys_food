package com.bapm.bzys.newBzys_food.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.bapm.bzys.newBzys_food.util.LoadWebUtils;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fs-ljh on 2017/6/2.
 */
public class ActivityOperatingChart extends BaseActivity{
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu_home)
    ImageView ivMenuHome;
    @BindView(R.id.tv_menu_home)
    TextView tvMenuHome;
    @BindView(R.id.layout_menu_home)
    AutoLinearLayout layoutMenuHome;
    @BindView(R.id.iv_menu_order)
    ImageView ivMenuOrder;
    @BindView(R.id.tv_menu_order)
    TextView tvMenuOrder;
    @BindView(R.id.layout_menu_order)
    AutoLinearLayout layoutMenuOrder;
    private LoadWebUtils loadWebUtils;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operating_chart);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("what").equals("operating")) {
            tvTitle.setText("经营概况");
            loadWebUtils = new LoadWebUtils(web, ActivityOperatingChart.this,
//                    "http://192.168.3.173:34567/Echarts/Index");
                    "http://fsshdemo.bzys.cn/catering/Echarts/Index");
        } else {
            tvTitle.setText("浏览报表");
            loadWebUtils = new LoadWebUtils(web, ActivityOperatingChart.this,
                    "http://fsshdemo.bzys.cn/catering/Echarts/Browse");
        }
        loadWebUtils.loadWeb();
    }

    /**
     * 点击底部菜单事件
     *
     * @param v
     */
    public void clickMenu(View v) {
        switch (v.getId()) {
            case R.id.layout_menu_home:
                this.finish();
                break;
            case R.id.layout_menu_order:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("isOrder", "order");
                startActivity(intent);
                ActivityManager.getInstance().finishAllActivity();
                break;
        }
    }

}
