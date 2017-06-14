package com.bapm.bzys.newBzys_food.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fs-ljh on 2017/6/6.
 */

public class TableSale implements Serializable{

    /**
     * OrderTotalCount : 10
     * BrowserTotalCount : 10
     * SalesTotalCount : 1438
     * TableSalesInfo : [{"PromotionName":"测试","PromotionNo":"110","OrderCount":0,"BrowserCount":0,"OrderPrice":0},{"PromotionName":"测试测试","PromotionNo":"123","OrderCount":10,"BrowserCount":10,"OrderPrice":1438}]
     */

    private int OrderTotalCount;
    private int BrowserTotalCount;
    private int SalesTotalCount;
    private List<TableSalesInfoBean> TableSalesInfo;

    public int getOrderTotalCount() {
        return OrderTotalCount;
    }

    public void setOrderTotalCount(int OrderTotalCount) {
        this.OrderTotalCount = OrderTotalCount;
    }

    public int getBrowserTotalCount() {
        return BrowserTotalCount;
    }

    public void setBrowserTotalCount(int BrowserTotalCount) {
        this.BrowserTotalCount = BrowserTotalCount;
    }

    public int getSalesTotalCount() {
        return SalesTotalCount;
    }

    public void setSalesTotalCount(int SalesTotalCount) {
        this.SalesTotalCount = SalesTotalCount;
    }

    public List<TableSalesInfoBean> getTableSalesInfo() {
        return TableSalesInfo;
    }

    public void setTableSalesInfo(List<TableSalesInfoBean> TableSalesInfo) {
        this.TableSalesInfo = TableSalesInfo;
    }

    public static class TableSalesInfoBean {
        /**
         * PromotionName : 测试
         * PromotionNo : 110
         * OrderCount : 0
         * BrowserCount : 0
         * OrderPrice : 0
         */

        private String PromotionName;
        private String PromotionNo;
        private int OrderCount;
        private int BrowserCount;
        private int OrderPrice;

        public String getPromotionName() {
            return PromotionName;
        }

        public void setPromotionName(String PromotionName) {
            this.PromotionName = PromotionName;
        }

        public String getPromotionNo() {
            return PromotionNo;
        }

        public void setPromotionNo(String PromotionNo) {
            this.PromotionNo = PromotionNo;
        }

        public int getOrderCount() {
            return OrderCount;
        }

        public void setOrderCount(int OrderCount) {
            this.OrderCount = OrderCount;
        }

        public int getBrowserCount() {
            return BrowserCount;
        }

        public void setBrowserCount(int BrowserCount) {
            this.BrowserCount = BrowserCount;
        }

        public int getOrderPrice() {
            return OrderPrice;
        }

        public void setOrderPrice(int OrderPrice) {
            this.OrderPrice = OrderPrice;
        }
    }
}
