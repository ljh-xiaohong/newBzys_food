package com.bapm.bzys.newBzys_food.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.base.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fs-ljh on 2017/6/9.
 */
public class ActivitySettingPrint extends BaseActivity {

    @BindView(R.id.layout_title_bar)
    AutoRelativeLayout layoutTitleBar;
    @BindView(R.id.print_switch)
    CheckBox printSwitch;
    @BindView(R.id.print_money_switch)
    CheckBox printMoneySwitch;
    @BindView(R.id.img_down)
    ImageView imgDown;
    @BindView(R.id.tv_money_count)
    TextView tvMoneyCount;
    @BindView(R.id.lay_money_print)
    AutoRelativeLayout layMoneyPrint;
    @BindView(R.id.print_cook_all_switch)
    CheckBox printCookAllSwitch;
    @BindView(R.id.img_down1)
    ImageView imgDown1;
    @BindView(R.id.tv_cook_all_count)
    TextView tvCookAllCount;
    @BindView(R.id.lay_cook_all_print)
    AutoRelativeLayout layCookAllPrint;
    @BindView(R.id.print_one_switch)
    CheckBox printOneSwitch;
    @BindView(R.id.img_down2)
    ImageView imgDown2;
    @BindView(R.id.tv_cook_one_count)
    TextView tvCookOneCount;
    @BindView(R.id.lay_cook_one_print)
    AutoRelativeLayout layCookOnePrint;
    @BindView(R.id.lay_set_print_count)
    AutoLinearLayout laySetPrintCount;
    @BindView(R.id.lay_print)
    AutoRelativeLayout layPrint;
    @BindView(R.id.lay_end_cook_print_switch)
    CheckBox layEndCookPrintSwitch;
    @BindView(R.id.print_end_cook_switch)
    CheckBox printEndCookSwitch;
    @BindView(R.id.img_down3)
    ImageView imgDown3;
    @BindView(R.id.tv_end_cook_count)
    TextView tvEndCookCount;
    @BindView(R.id.lay_end_cook_money_print)
    AutoRelativeLayout layEndCookMoneyPrint;
    @BindView(R.id.print_end_cook_cook_all_switch)
    CheckBox printEndCookCookAllSwitch;
    @BindView(R.id.img_down4)
    ImageView imgDown4;
    @BindView(R.id.tv_end_cook_cook_all_count)
    TextView tvEndCookCookAllCount;
    @BindView(R.id.lay_end_cook_cook_all_print)
    AutoRelativeLayout layEndCookCookAllPrint;
    @BindView(R.id.print_end_cook_one_switch)
    CheckBox printEndCookOneSwitch;
    @BindView(R.id.img_down5)
    ImageView imgDown5;
    @BindView(R.id.tv_end_cook_cook_one_count)
    TextView tvEndCookCookOneCount;
    @BindView(R.id.lay_end_cook_cook_one_print)
    AutoRelativeLayout layEndCookCookOnePrint;
    @BindView(R.id.lay_end_cook_count)
    AutoLinearLayout layEndCookCount;
    @BindView(R.id.lay_end_cook_print)
    AutoRelativeLayout layEndCookPrint;
    @BindView(R.id.lay_pay_print_switch)
    CheckBox layPayPrintSwitch;
    @BindView(R.id.lay_pay_print)
    AutoRelativeLayout layPayPrint;
    @BindView(R.id.choose_layout)
    AutoLinearLayout chooseLayout;
    @BindView(R.id.print_pay_switch)
    CheckBox printPaySwitch;
    @BindView(R.id.img_down6)
    ImageView imgDown6;
    @BindView(R.id.tv_pay_count)
    TextView tvPayCount;
    @BindView(R.id.lay_pay_money_print)
    AutoRelativeLayout layPayMoneyPrint;
    @BindView(R.id.print_pay_cook_all_switch)
    CheckBox printPayCookAllSwitch;
    @BindView(R.id.img_down7)
    ImageView imgDown7;
    @BindView(R.id.tv_pay_cook_all_count)
    TextView tvPayCookAllCount;
    @BindView(R.id.lay_pay_cook_all_print)
    AutoRelativeLayout layPayCookAllPrint;
    @BindView(R.id.print_pay_one_switch)
    CheckBox printPayOneSwitch;
    @BindView(R.id.img_down8)
    ImageView imgDown8;
    @BindView(R.id.tv_pay_cook_one_count)
    TextView tvPayCookOneCount;
    @BindView(R.id.lay_pay_cook_one_print)
    AutoRelativeLayout layPayCookOnePrint;
    @BindView(R.id.lay_pay_count)
    AutoLinearLayout layPayCount;
    private int what;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_print);
        ButterKnife.bind(this);
        for (int i = 1; i <= 8; i++) {
            types.add(i + "份");
        }
    }

    private View views[];
    private View popupView;
    private PopupWindow window;
    List<String> types = new ArrayList<String>();
    private String money_count;
    private String cook_all_count;
    private String cook_one_count;

    /**
     * 菜品大类选择器
     */
    private void showToolsView(List<String> types) {
        LayoutInflater inflater = LayoutInflater.from(this);
        popupView = getLayoutInflater().inflate(R.layout.select_layout_popwindow, null);
        LinearLayout tools = (LinearLayout) popupView.findViewById(R.id.tools);
        int size = tools.getChildCount();
        for (int i = 0; i < size; i++) {
            tools.removeViewAt(0);
        }
        views = new View[types.size()];
        for (int i = 0; i < types.size(); i++) {
            View view = inflater.inflate(R.layout.select_item, null);
            view.setTag(i);
            view.setOnClickListener(toolsItemListener);
            ImageView select_img = (ImageView) view.findViewById(R.id.select_img);
            TextView select_tv = (TextView) view.findViewById(R.id.select_tv);
            select_tv.setText(types.get(i));
            tools.addView(view);
            views[i] = view;
        }
        if (what == 1) {
            if (money_count == null) {
                changeTextColor(0);
            }
            if (types.indexOf(money_count) == -1 && types.size() > 0) {
                changeTextColor(0);
            } else {
                int index = types.indexOf(money_count);
                if (index < types.size() && index > -1)
                    changeTextColor(index);
            }
        } else if (what == 2) {
            if (cook_all_count == null) {
                changeTextColor(0);
            }
            if (types.indexOf(cook_all_count) == -1 && types.size() > 0) {
                changeTextColor(0);
            } else {
                int index = types.indexOf(cook_all_count);
                if (index < types.size() && index > -1)
                    changeTextColor(index);
            }
        } else {
            if (cook_one_count == null) {
                changeTextColor(0);
            }
            if (types.indexOf(cook_one_count) == -1 && types.size() > 0) {
                changeTextColor(0);
            } else {
                int index = types.indexOf(cook_one_count);
                if (index < types.size() && index > -1)
                    changeTextColor(index);
            }
        }

        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() / 2);
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
        window.setOnDismissListener(new poponDismissListener());
        // 以下拉的方式显示，并且可以设置显示的位置
        window.showAtLocation(findViewById(R.id.choose_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 改变textView的颜色
     */
    private void changeTextColor(int position) {
        if (views.length < 0)
            return;
        for (int i = 0; i < views.length; i++) {
            if (i == position) {
                ((TextView) views[i].findViewById(R.id.select_tv)).setTextColor(getResources().getColor(R.color.edt_hint_orange));
                ((ImageView) views[i].findViewById(R.id.select_img)).setVisibility(View.VISIBLE);
            } else {
                ((TextView) views[i].findViewById(R.id.select_tv)).setTextColor(getResources().getColor(R.color.black3));
                ((ImageView) views[i].findViewById(R.id.select_img)).setVisibility(View.GONE);
            }
        }
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (Integer) v.getTag();
            changeTextColor(tag);
//                ed_type.setText(typeNames.get(tag));
//                tv_type_id.setText(typeIds.get(tag));
//                tvTypeId=typeIds.get(tag);
//                typeName=typeNames.get(tag);
            if (what == 1) {
                money_count = types.get(tag);
                tvMoneyCount.setText(types.get(tag));
            } else if (what == 2) {
                cook_all_count = types.get(tag);
                tvCookAllCount.setText(types.get(tag));
            } else {
                cook_one_count = types.get(tag);
                tvCookOneCount.setText(types.get(tag));
            }
            window.dismiss();
        }
    };

    @OnClick({R.id.lay_money_print, R.id.lay_cook_all_print, R.id.lay_cook_one_print, R.id.lay_print, R.id.lay_end_cook_print, R.id.lay_pay_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_cook_all_print:
                what = 2;
                showToolsView(types);
                break;
            case R.id.lay_cook_one_print:
                what = 3;
                showToolsView(types);
                break;
            case R.id.lay_end_cook_print:
                layEndCookCount.setVisibility(View.VISIBLE);
                break;
            case R.id.lay_pay_print:
                layPayCount.setVisibility(View.VISIBLE);
                break;
            case R.id.lay_print:
                laySetPrintCount.setVisibility(View.VISIBLE);
                break;
            case R.id.lay_money_print:
                what = 1;
                showToolsView(types);
                break;
        }
    }
}
