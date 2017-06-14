package com.bapm.bzys.newBzys_food.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fs-ljh on 2017/6/7.
 */

public class ActivitySettingBuleTooth extends BaseActivity {
    @BindView(R.id.print_setting_lay)
    AutoLinearLayout printSettingLay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_buletooth);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.print_setting_lay)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.print_setting_lay:
                Intent intent=new Intent(ActivitySettingBuleTooth.this,ActivityConnectBuleTooth.class);
                startActivity(intent);
                break;
        }
    }

}
