package com.bapm.bzys.newBzys_food.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.zhy.autolayout.AutoRelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fs-ljh on 2017/6/2.
 */
public class ActivityChart extends BaseActivity {

    @BindView(R.id.layout_operating)
    AutoRelativeLayout layoutOperating;
    @BindView(R.id.layout_browse)
    AutoRelativeLayout layoutBrowse;
    @BindView(R.id.layout_sales)
    AutoRelativeLayout layoutSales;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.layout_operating, R.id.layout_browse, R.id.layout_sales})
    public void onViewClicked(View view) {
        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.layout_operating:
                intent.setClass(ActivityChart.this,ActivityOperatingChart.class);
                intent.putExtra("what","operating");
                break;
            case R.id.layout_browse:
                intent.setClass(ActivityChart.this,ActivityOperatingChart.class);
                intent.putExtra("what","browse");
                break;
            case R.id.layout_sales:
                intent.setClass(ActivityChart.this,ActivitySalesChart.class);
                break;
        }
        startActivity(intent);
    }
    /**
     * 点击底部菜单事件
     * @param v
     */
    public void clickMenu(View v){
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
