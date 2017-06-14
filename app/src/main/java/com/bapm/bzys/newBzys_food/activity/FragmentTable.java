package com.bapm.bzys.newBzys_food.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bapm.bzys.newBzys_food.R;
import com.bapm.bzys.newBzys_food.model.FoodSale;
import com.bapm.bzys.newBzys_food.model.TableSale;
import com.bapm.bzys.newBzys_food.network.DadanUrl;
import com.bapm.bzys.newBzys_food.network.HttpUtil;
import com.bapm.bzys.newBzys_food.network.function.interf.Function;
import com.bapm.bzys.newBzys_food.network.function.interf.FunctionManager;
import com.bapm.bzys.newBzys_food.util.ActivityManager;
import com.bapm.bzys.newBzys_food.util.DadanPreference;
import com.bapm.bzys.newBzys_food.util.LoginFailUtils;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullListView;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullListViewAdapter;
import com.bapm.bzys.newBzys_food.view.nestlistview.NestFullViewHolder;
import com.bapm.bzys.newBzys_food.widget.DadanArcDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
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
public class FragmentTable extends Fragment implements Function {
    @BindView(R.id.tv_today)
    TextView tvToday;
    @BindView(R.id.tv_yesterday)
    TextView tvYesterday;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_order_count)
    TextView tvOrderCount;
    @BindView(R.id.tv_sale_money)
    TextView tvSaleMoney;
    @BindView(R.id.tv_watch_count)
    TextView tvWatchCount;
    @BindView(R.id.zListView)
    NestFullListView zListView;
    @BindView(R.id.scroll_right)
    ScrollView scrollRight;
    private View view;
    private FunctionManager manager;
    private DadanArcDialog loadDialog;
    private Unbinder unbinder;
    private List<TableSale.TableSalesInfoBean> WeekDayTypes = new ArrayList<TableSale.TableSalesInfoBean>();
    private int currentList;
    private LoginFailUtils failUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {// 优化View减少View的创建次数
            view = inflater.inflate(R.layout.fragment_table, null);
        }
        manager = this.init(this.getContext());
        manager.registFunClass(FragmentTable.class);
        loadDialog = new DadanArcDialog(getActivity());
        loadDialog.setCancelable(false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        initData("1");
    }
    public void initData(String day){
        loadDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("selectday",day);
        manager.getTableSaleList(params, this);
    }
    int i = 0;
    private void initView(final List<TableSale.TableSalesInfoBean> DayTypes) {
        if (i != 0) {
            zListView.removedCahce();
        }
        zListView.setAdapter(new NestFullListViewAdapter<TableSale.TableSalesInfoBean>(R.layout.fragment_chart_right_table_item, DayTypes) {
            @Override
            public void onBind(int pos, TableSale.TableSalesInfoBean goodsSales, NestFullViewHolder holder) {
                setOrderData(holder, goodsSales);
                i = 1;
            }
        });
        loadDialog.dismiss();
    }
    private void setOrderData(final NestFullViewHolder holder, final TableSale.TableSalesInfoBean goodsSales) {
        holder.setText(R.id.tv_table_name, goodsSales.getPromotionName());
        holder.setText(R.id.tv_table_no, goodsSales.getPromotionNo() + "");
        holder.setText(R.id.tv_order_no, goodsSales.getOrderCount() + "");
        holder.setText(R.id.tv_watch_no, goodsSales.getBrowserCount() + "");
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        holder.setText(R.id.tv_money_no, nf.format(goodsSales.getOrderPrice())+ "");
    }
    @Override
    public FunctionManager init(Context context) {
        return new FunctionManager(context);
    }

    @Override
    public void onSuccess(int requstCode, JSONObject result) {
        Log.e("result",result.toString());
        switch (requstCode) {
            case DadanUrl.GET_TABLE_SALE_LIST_CODE:
                WeekDayTypes.clear();
                Gson gson = new Gson();
                TableSale tableSale = gson.fromJson(result.toString(), TableSale.class);
                WeekDayTypes.addAll(tableSale.getTableSalesInfo());
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                tvOrderCount.setText(nf.format(tableSale.getOrderTotalCount()) + "");
                tvSaleMoney.setText(nf.format(tableSale.getSalesTotalCount())+ "");
                tvWatchCount.setText(nf.format(tableSale.getBrowserTotalCount()) + "");
//                showToolsView(TodayTypes);
                initView(WeekDayTypes);
                break;
            case DadanUrl.USER_LOGIN_AGAIN_REQUEST_CODE:
                if (result.optString("LogionCode").equals("1")) {
                    DadanPreference.getInstance(getActivity()).setTicket(result.optString("Ticket"));
                    onResume();
                    failUtils.getId();
                } else if (result.optString("LogionCode").equals("-1")) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra("LogionCode", "-1");
                    startActivity(intent);
                    ActivityManager.getInstance().finishAllActivity();
                }
                break;
            case DadanUrl.GET_REGISTRATION_CODE:
                try {
                    Log.e("GET_REGISTRATION_CODE",result.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onSuccess(int requstCode, JSONArray result) {
        Log.e("result",result.toString());
    }

    @Override
    public void onFaile(int requestCode, int status, String msg) {
        loadDialog.dismiss();
        failUtils=new LoginFailUtils(requestCode,getActivity(),manager,FragmentTable.this);
        failUtils.onFaile();
        Log.e("result",msg.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.unregistFunctionClass(FragmentTable.class);
    }
    @OnClick({R.id.tv_today, R.id.tv_yesterday, R.id.tv_week, R.id.tv_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_today:
                tvToday.setBackgroundResource(R.drawable.tv_date_check);
                tvYesterday.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvMonth.setBackgroundResource(0);
                tvToday.setTextColor(getResources().getColor(R.color.white));
                tvYesterday.setTextColor(getResources().getColor(R.color.tv_date));
                tvWeek.setTextColor(getResources().getColor(R.color.tv_date));
                tvMonth.setTextColor(getResources().getColor(R.color.tv_date));
                initData("1");
                break;
            case R.id.tv_yesterday:
                tvYesterday.setBackgroundResource(R.drawable.tv_date_check);
                tvToday.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvMonth.setBackgroundResource(0);
                tvYesterday.setTextColor(getResources().getColor(R.color.white));
                tvToday.setTextColor(getResources().getColor(R.color.tv_date));
                tvWeek.setTextColor(getResources().getColor(R.color.tv_date));
                tvMonth.setTextColor(getResources().getColor(R.color.tv_date));
                initData("2");
                break;
            case R.id.tv_week:
                tvWeek.setBackgroundResource(R.drawable.tv_date_check);
                tvToday.setBackgroundResource(0);
                tvYesterday.setBackgroundResource(0);
                tvMonth.setBackgroundResource(0);
                tvWeek.setTextColor(getResources().getColor(R.color.white));
                tvYesterday.setTextColor(getResources().getColor(R.color.tv_date));
                tvToday.setTextColor(getResources().getColor(R.color.tv_date));
                tvMonth.setTextColor(getResources().getColor(R.color.tv_date));
                initData("3");
                break;
            case R.id.tv_month:
                tvMonth.setBackgroundResource(R.drawable.tv_date_check);
                tvToday.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvWeek.setBackgroundResource(0);
                tvMonth.setTextColor(getResources().getColor(R.color.white));
                tvYesterday.setTextColor(getResources().getColor(R.color.tv_date));
                tvWeek.setTextColor(getResources().getColor(R.color.tv_date));
                tvToday.setTextColor(getResources().getColor(R.color.tv_date));
                initData("4");
                break;
        }
    }
}
