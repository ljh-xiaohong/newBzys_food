package com.bapm.bzys.newBzys_food.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fs-ljh on 2017/6/2.
 */
public class ActivitySalesChart extends BaseActivity {
    @BindView(R.id.iv_menu_food)
    ImageView iv_menu_food;
    @BindView(R.id.tv_menu_food)
    TextView tv_menu_food;
    @BindView(R.id.layout_menu_food)
    AutoLinearLayout layoutMenuFood;
    @BindView(R.id.iv_menu_table)
    ImageView iv_menu_table;
    @BindView(R.id.tv_menu_table)
    TextView tv_menu_table;
    @BindView(R.id.layout_menu_table)
    AutoLinearLayout layoutMenuTable;
    private FragmentFood fragFood;
    private FragmentTable fragTable;
    // 布局管理器
    private FragmentManager fragManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_chart);
        ButterKnife.bind(this);
        fragManager = getSupportFragmentManager();
        clickMenu(findViewById(R.id.layout_menu_food));
    }

    /**
     * 点击底部菜单事件
     *
     * @param v
     */
    public void clickMenu(View v) {
        FragmentTransaction trans = fragManager.beginTransaction();
        int vID = v.getId();
        // 设置menu样式
        setMenuStyle(vID);
        // 隐藏所有的fragment
        hideFrament(trans);
        // 设置Fragment
        setFragment(vID, trans);
//        if (intPromotionID!=0) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("intPromotionID",getIntent().getIntExtra("intPromotionID",0));
//            fragTable.setArguments(bundle);
//        }else{
//
//        }
        trans.commit();
    }

    /**
     * 隐藏所有的fragment(编程初始化状态)
     *
     * @param trans
     */
    private void hideFrament(FragmentTransaction trans) {
        if (fragFood != null) {
            trans.hide(fragFood);
        }
        if (fragTable != null) {
            trans.hide(fragTable);
        }
    }

    /**
     * 设置menu样式
     *
     * @param vID
     * @param trans
     */
    private void setMenuStyle(int id) {
        // 主页
        if (id == R.id.layout_menu_food) {
            iv_menu_food.setImageDrawable(getResources().getDrawable(R.mipmap.food_press));
            tv_menu_food.setTextColor(getResources().getColor(R.color.menu_click));
        } else {
            iv_menu_food.setImageDrawable(getResources().getDrawable(R.mipmap.food_default));
            tv_menu_food.setTextColor(getResources().getColor(R.color.menu_nomarl));
        }
        // 订单
        if (id == R.id.layout_menu_table) {
            iv_menu_table.setImageDrawable(getResources().getDrawable(R.mipmap.table_press));
            tv_menu_table.setTextColor(getResources().getColor(R.color.menu_click));
        } else {
            iv_menu_table.setImageDrawable(getResources().getDrawable(R.mipmap.table_default));
            tv_menu_table.setTextColor(getResources().getColor(R.color.menu_nomarl));
        }
    }

    /**
     * 设置Fragment
     *
     * @param vID
     * @param trans
     */
    private void setFragment(int vID, FragmentTransaction trans) {
        switch (vID) {
            case R.id.layout_menu_food:
                if (fragFood == null) {
                    fragFood = new FragmentFood();
                    trans.add(R.id.content, fragFood);
                } else {
                    trans.show(fragFood);
                }
                break;
            case R.id.layout_menu_table:
                if (fragTable == null) {
                    fragTable = new FragmentTable();
                    trans.add(R.id.content, fragTable);
                } else {
                    trans.show(fragTable);
                }
                break;
            default:
                break;

        }
    }
}
