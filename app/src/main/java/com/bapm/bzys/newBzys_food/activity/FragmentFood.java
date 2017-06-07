package com.bapm.bzys.newBzys_food.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.model.FoodSale;
import com.bapm.bzys.newBzys_food.model.sort;
import com.bapm.bzys.newBzys_food.network.DadanUrl;
import com.bapm.bzys.newBzys_food.network.HttpUtil;
import com.bapm.bzys.newBzys_food.network.function.interf.Function;
import com.bapm.bzys.newBzys_food.network.function.interf.FunctionManager;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.bapm.bzys.newBzys_food.util.CustomToast;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullListView;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullListViewAdapter;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullViewHolder;
import com.bapm.bzys.newBzys_food.widget.DadanArcDialog;
import com.google.gson.Gson;
import com.zhy.autolayout.AutoLinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * Created by fs-ljh on 2017/6/3.
 */
public class FragmentFood extends Fragment implements Function {
    @BindView(R.id.tv_today)
    TextView tvToday;
    @BindView(R.id.tv_yesterday)
    TextView tvYesterday;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_all_sale_count)
    TextView tvAllSaleCount;
    @BindView(R.id.tools)
    AutoLinearLayout tools;
    @BindView(R.id.tools_scrlllview)
    ScrollView toolsScrlllview;
    @BindView(R.id.zListView)
    NestFullListView zListView;
    @BindView(R.id.scroll_right)
    ScrollView scrollRight;
    @BindView(R.id.tv_food_type_name)
    TextView tvFoodTypeName;
    @BindView(R.id.tv_food_type_count)
    TextView tvFoodTypeCount;
    @BindView(R.id.img_upanddown)
    ImageView imgUpanddown;
    @BindView(R.id.lay_upanddown)
    AutoLinearLayout layUpanddown;
    private Unbinder unbinder;
    private View view;
    private FunctionManager manager;
    private DadanArcDialog loadDialog;
    private List<FoodSale.WeekDayBean> WeekDayTypes = new ArrayList<FoodSale.WeekDayBean>();
    private List<FoodSale.WeekDayBean> MonthDayTypes = new ArrayList<FoodSale.WeekDayBean>();
    private List<FoodSale.WeekDayBean> YesTodayTypes = new ArrayList<FoodSale.WeekDayBean>();
    private List<FoodSale.WeekDayBean> TodayTypes = new ArrayList<FoodSale.WeekDayBean>();
    private int currentList;
    private boolean isPress = true;
    private int maxSaleCount;
    private int tager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {// 优化View减少View的创建次数
            view = inflater.inflate(R.layout.fragment_food, null);
        }
        manager = this.init(this.getContext());
        manager.registFunClass(FragmentFood.class);
        loadDialog = new DadanArcDialog(getActivity());
        loadDialog.setCancelable(false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        manager.getFoodSaleList(params, this);
    }

    @Override
    public FunctionManager init(Context context) {
        return new FunctionManager(context);
    }

    @Override
    public void onSuccess(int requstCode, JSONObject result) {
        switch (requstCode) {
            case DadanUrl.GET_FOOD_SALE_LIST_CODE:
                WeekDayTypes.clear();
                MonthDayTypes.clear();
                YesTodayTypes.clear();
                TodayTypes.clear();
                Gson gson = new Gson();
                FoodSale foodSale = gson.fromJson(result.toString(), FoodSale.class);
                WeekDayTypes.addAll(foodSale.getWeekDay());
                MonthDayTypes.addAll(foodSale.getMonthDay());
                YesTodayTypes.addAll(foodSale.getYesToday());
                TodayTypes.addAll(foodSale.getToday());
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                tvAllSaleCount.setText(nf.format(foodSale.getWeekDay().get(currentList).getSalesTotalCount()) + "");
                showToolsView(TodayTypes);
                initView(TodayTypes);
                break;
            case DadanUrl.USER_LOGIN_AGAIN_REQUEST_CODE:
                if (result.optString("LogionCode").equals("1")) {
                    DadanPreference.getInstance(getActivity()).setTicket(result.optString("Ticket"));
                    onResume();
                } else if (result.optString("LogionCode").equals("-1")) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("LogionCode", "-1");
                    startActivity(intent);
                    ActivityManager.getInstance().finishAllActivity();
                }
                break;
        }
    }

    int i = 0;
    //加载报表
    private void initView(final List<FoodSale.WeekDayBean>DayTypes) {
//        setSrollView();
//        setRefresh();
//        delectTips.setOnClickListener(this);
        if (DayTypes.size()==0){
            return;
        }
        tvFoodTypeName.setText(DayTypes.get(currentList).getGoodsTypeNmae());
        tvFoodTypeCount.setText("/" + DayTypes.get(currentList).getTypeSaleCount() + "份");

        if (i != 0) {
            zListView.removedCahce();
        }
        //判断报表排序方式
        if (isPress) {
            imgUpanddown.setImageResource(R.mipmap.upanddown_default);
            Collections.sort(DayTypes.get(currentList).getGoodsSales(), new sort("desc"));
            maxSaleCount = DayTypes.get(currentList).getGoodsSales().get(0).getSaleCount();
        }else{
            imgUpanddown.setImageResource(R.mipmap.upanddown_press);
            Collections.sort(DayTypes.get(currentList).getGoodsSales(), new sort("asc"));
            maxSaleCount = DayTypes.get(currentList).getGoodsSales().get(DayTypes.get(currentList).getGoodsSales().size()-1).getSaleCount();
        }
        zListView.setAdapter(new NestFullListViewAdapter<FoodSale.WeekDayBean.GoodsSalesBean>(R.layout.fragment_chart_right_food_item, DayTypes.get(currentList).getGoodsSales()) {
            @Override
            public void onBind(int pos, FoodSale.WeekDayBean.GoodsSalesBean goodsSales, NestFullViewHolder holder) {
                setOrderData(holder, goodsSales);
                i = 1;
            }
        });
        //报表排序
        layUpanddown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPress) {
                    imgUpanddown.setImageResource(R.mipmap.upanddown_press);
                    isPress = false;
                    if (i != 0) {
                        zListView.removedCahce();
                    }
                    Collections.sort(DayTypes.get(currentList).getGoodsSales(), new sort("asc"));
                    zListView.setAdapter(new NestFullListViewAdapter<FoodSale.WeekDayBean.GoodsSalesBean>(R.layout.fragment_chart_right_food_item, DayTypes.get(currentList).getGoodsSales()) {
                        @Override
                        public void onBind(int pos, FoodSale.WeekDayBean.GoodsSalesBean goodsSales, NestFullViewHolder holder) {
                            setOrderData(holder, goodsSales);
                            i = 1;
                        }
                    });
                } else {
                    imgUpanddown.setImageResource(R.mipmap.upanddown_default);
                    isPress = true;
                    if (i != 0) {
                        zListView.removedCahce();
                    }
                    Collections.sort(DayTypes.get(currentList).getGoodsSales(), new sort("desc"));
                    zListView.setAdapter(new NestFullListViewAdapter<FoodSale.WeekDayBean.GoodsSalesBean>(R.layout.fragment_chart_right_food_item, DayTypes.get(currentList).getGoodsSales()) {
                        @Override
                        public void onBind(int pos, FoodSale.WeekDayBean.GoodsSalesBean goodsSales, NestFullViewHolder holder) {
                            setOrderData(holder, goodsSales);
                            i = 1;
                        }
                    });
                }
            }
        });

        loadDialog.dismiss();
    }
    //菜品加载
    private void setOrderData(final NestFullViewHolder holder, final FoodSale.WeekDayBean.GoodsSalesBean goodsSales) {
        //根据数据改变进度条
        DisplayMetrics displayMetrics =getActivity().getResources().getDisplayMetrics();
        int rightWidth =(int)(holder.getView(R.id.chart_long).getLayoutParams().width*displayMetrics.density/3.5);
        final ViewGroup.LayoutParams params = holder.getView(R.id.progressBar).getLayoutParams();
        if (goodsSales.getSaleCount() != 0) {
            params.width =  goodsSales.getSaleCount() * rightWidth/maxSaleCount;
        } else {
            params.width=0;
        }
        holder.getView(R.id.progressBar).post(new Runnable() {
            @Override
            public void run() {
                holder.getView(R.id.progressBar).setLayoutParams(params);
            }
        });
        holder.setText(R.id.tv_food_name, goodsSales.getGoodsName());
        holder.setText(R.id.tv_food_count, goodsSales.getSaleCount() + "");
        if (holder.getView(R.id.progressBar).getLayoutParams().width==0){
            holder.setText(R.id.tv_out_food_count, goodsSales.getSaleCount() + "");
            holder.setVisible(R.id.tv_out_food_count,true);
            holder.setVisible(R.id.tv_food_count,false);
            holder.setVisible(R.id.progressBar,false);
        }else if (holder.getView(R.id.progressBar).getLayoutParams().width<=50){
            holder.setText(R.id.tv_out_food_count, goodsSales.getSaleCount() + "");
            holder.setVisible(R.id.tv_out_food_count,true);
            holder.setVisible(R.id.tv_food_count,false);
        }

    }

    /*
       * 左边列表
       * */
    private LayoutInflater inflater;
    private View views[];
    private TextView tv_type_name;
    private TextView tv_type_number;
    private ImageView iv_left;

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView(List<FoodSale.WeekDayBean> WeekDayTypes) {
        inflater = LayoutInflater.from(getActivity());
        int size = tools.getChildCount();
        for (int i = 0; i < size; i++) {
            tools.removeViewAt(0);
        }
        views = new View[WeekDayTypes.size()];
        for (int i = 0; i < WeekDayTypes.size(); i++) {
            View view = inflater.inflate(R.layout.fragment_chart_left_item, null);
            view.setTag(i);
            view.setOnClickListener(toolsItemListener);
            tv_type_name = (TextView) view.findViewById(R.id.tv_type_name);
            tv_type_number = (TextView) view.findViewById(R.id.tv_type_number);
            iv_left = (ImageView) view.findViewById(R.id.iv_left);
            tv_type_name.setText(WeekDayTypes.get(i).getGoodsTypeNmae());
            tv_type_number.setText(WeekDayTypes.get(i).getTypeSaleCount() + "份");
            tools.addView(view);
            views[i] = view;
        }
        changeTextColor(currentList);
    }

    /**
     * 改变textView的颜色
     */
    private void changeTextColor(int position) {
        if (views.length < 0)
            return;
        for (int i = 0; i < views.length; i++) {
            if (i == position) {
                views[i].setBackgroundResource(R.color.fragment_order_left_item_nor_bg);
                ((TextView) views[i].findViewById(R.id.tv_type_name)).setTextColor(getResources().getColor(R.color.red));
                ((TextView) views[i].findViewById(R.id.tv_type_number)).setTextColor(getResources().getColor(R.color.red));
                ((ImageView) views[i].findViewById(R.id.iv_left)).setVisibility(View.VISIBLE);
            } else {
                views[i].setBackgroundResource(R.color.fragment_order_left_item_select_bg);
                ((TextView) views[i].findViewById(R.id.tv_type_name)).setTextColor(getResources().getColor(R.color.black3));
                ((TextView) views[i].findViewById(R.id.tv_type_number)).setTextColor(getResources().getColor(R.color.black3));
                ((ImageView) views[i].findViewById(R.id.iv_left)).setVisibility(View.GONE);
            }
        }
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (Integer) v.getTag();
            currentList = tag;
            changeTextColor(tag);
            if (tager==0){
                initView(TodayTypes);
            }else if(tager==1){
                initView(YesTodayTypes);
            }else if(tager==2){
                initView(WeekDayTypes);
            }else if(tager==3){
                initView(MonthDayTypes);
            }
        }
    };

    @Override
    public void onSuccess(int requstCode, JSONArray result) {
        Log.e("result", result + "");
        switch (requstCode) {
            case DadanUrl.GET_FOOD_SALE_LIST_CODE:
                break;

        }
    }

    @Override
    public void onFaile(int requestCode, int status, String msg) {
        loadDialog.dismiss();
        if (requestCode == HttpUtil.ST_ACCOUNT_OTHER_LOGIN_FAILE || requestCode == 233 || requestCode == 232) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("DEVICE_ID", ((TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE)).getDeviceId());
            manager.loginAgain(params, this);
        }
        if(requestCode==245){
            CustomToast.showToast(getActivity(),"网络请求超时");
        }
        Log.e("msg", msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.unregistFunctionClass(FragmentFood.class);
    }

    @OnClick({R.id.tv_today, R.id.tv_yesterday, R.id.tv_week, R.id.tv_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_today:
                tager=0;
                tvToday.setBackgroundResource(R.drawable.tv_date_check);
                tvYesterday.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvMonth.setBackgroundResource(0);
                tvToday.setTextColor(getResources().getColor(R.color.white));
                tvYesterday.setTextColor(getResources().getColor(R.color.tv_date));
                tvWeek.setTextColor(getResources().getColor(R.color.tv_date));
                tvMonth.setTextColor(getResources().getColor(R.color.tv_date));
                showToolsView(TodayTypes);
                initView(TodayTypes);
                break;
            case R.id.tv_yesterday:
                tager=1;
                tvYesterday.setBackgroundResource(R.drawable.tv_date_check);
                tvToday.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvMonth.setBackgroundResource(0);
                tvYesterday.setTextColor(getResources().getColor(R.color.white));
                tvToday.setTextColor(getResources().getColor(R.color.tv_date));
                tvWeek.setTextColor(getResources().getColor(R.color.tv_date));
                tvMonth.setTextColor(getResources().getColor(R.color.tv_date));
                showToolsView(YesTodayTypes);
                initView(YesTodayTypes);
                break;
            case R.id.tv_week:
                tager=2;
                tvWeek.setBackgroundResource(R.drawable.tv_date_check);
                tvToday.setBackgroundResource(0);
                tvYesterday.setBackgroundResource(0);
                tvMonth.setBackgroundResource(0);
                tvWeek.setTextColor(getResources().getColor(R.color.white));
                tvYesterday.setTextColor(getResources().getColor(R.color.tv_date));
                tvToday.setTextColor(getResources().getColor(R.color.tv_date));
                tvMonth.setTextColor(getResources().getColor(R.color.tv_date));
                showToolsView(WeekDayTypes);
                initView(WeekDayTypes);
                break;
            case R.id.tv_month:
                tager=3;
                tvMonth.setBackgroundResource(R.drawable.tv_date_check);
                tvToday.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvMonth.setTextColor(getResources().getColor(R.color.white));
                tvYesterday.setTextColor(getResources().getColor(R.color.tv_date));
                tvWeek.setTextColor(getResources().getColor(R.color.tv_date));
                tvToday.setTextColor(getResources().getColor(R.color.tv_date));
                showToolsView(MonthDayTypes);
                initView(MonthDayTypes);
                break;
        }
    }
}
