package com.bapm.bzys.newBzys_food.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fs-ljh on 2017/6/6.
 */

public class TableSale implements Serializable{

    /**
     * WeekDay : {"TableSalesInfo":[{"PromotionNo":"110","OrderPrice":0,"BrowserCount":5,"OrderCount":0,"PromotionName":"测试"},{"PromotionNo":"123","OrderPrice":5490,"BrowserCount":3,"OrderCount":3,"PromotionName":"测试测试"},{"PromotionNo":"111","OrderPrice":0,"BrowserCount":2,"OrderCount":0,"PromotionName":"哈哈哈哈哈"}],"OrderTotalCount":3,"SalesTotalCount":5490,"BrowserTotalCount":10}
     * MonthDay : {"TableSalesInfo":[{"PromotionNo":"110","OrderPrice":0,"BrowserCount":5,"OrderCount":0,"PromotionName":"测试"},{"PromotionNo":"123","OrderPrice":5490,"BrowserCount":3,"OrderCount":3,"PromotionName":"测试测试"},{"PromotionNo":"111","OrderPrice":0,"BrowserCount":2,"OrderCount":0,"PromotionName":"哈哈哈哈哈"}],"OrderTotalCount":3,"SalesTotalCount":5490,"BrowserTotalCount":10}
     * YesToday : {"TableSalesInfo":[{"PromotionNo":"110","OrderPrice":0,"BrowserCount":0,"OrderCount":0,"PromotionName":"测试"},{"PromotionNo":"123","OrderPrice":1830,"BrowserCount":1,"OrderCount":1,"PromotionName":"测试测试"},{"PromotionNo":"111","OrderPrice":0,"BrowserCount":0,"OrderCount":0,"PromotionName":"哈哈哈哈哈"}],"OrderTotalCount":1,"SalesTotalCount":1830,"BrowserTotalCount":1}
     * Today : {"TableSalesInfo":[{"PromotionNo":"110","OrderPrice":0,"BrowserCount":0,"OrderCount":0,"PromotionName":"测试"},{"PromotionNo":"123","OrderPrice":0,"BrowserCount":1,"OrderCount":0,"PromotionName":"测试测试"},{"PromotionNo":"111","OrderPrice":0,"BrowserCount":1,"OrderCount":0,"PromotionName":"哈哈哈哈哈"}],"OrderTotalCount":0,"SalesTotalCount":0,"BrowserTotalCount":2}
     */

    private WeekDayBean WeekDay;
    private WeekDayBean MonthDay;
    private WeekDayBean YesToday;
    private WeekDayBean Today;

    public WeekDayBean getWeekDay() {
        return WeekDay;
    }

    public void setWeekDay(WeekDayBean WeekDay) {
        this.WeekDay = WeekDay;
    }

    public WeekDayBean getMonthDay() {
        return MonthDay;
    }

    public void setMonthDay(WeekDayBean MonthDay) {
        this.MonthDay = MonthDay;
    }

    public WeekDayBean getYesToday() {
        return YesToday;
    }

    public void setYesToday(WeekDayBean YesToday) {
        this.YesToday = YesToday;
    }

    public WeekDayBean getToday() {
        return Today;
    }

    public void setToday(WeekDayBean Today) {
        this.Today = Today;
    }

    public static class WeekDayBean implements Serializable{
        /**
         * TableSalesInfo : [{"PromotionNo":"110","OrderPrice":0,"BrowserCount":5,"OrderCount":0,"PromotionName":"测试"},{"PromotionNo":"123","OrderPrice":5490,"BrowserCount":3,"OrderCount":3,"PromotionName":"测试测试"},{"PromotionNo":"111","OrderPrice":0,"BrowserCount":2,"OrderCount":0,"PromotionName":"哈哈哈哈哈"}]
         * OrderTotalCount : 3
         * SalesTotalCount : 5490
         * BrowserTotalCount : 10
         */

        private double OrderTotalCount;
        private double SalesTotalCount;
        private double BrowserTotalCount;
        private List<TableSalesInfoBean> TableSalesInfo;

        public double getOrderTotalCount() {
            return OrderTotalCount;
        }

        public void setOrderTotalCount(double OrderTotalCount) {
            this.OrderTotalCount = OrderTotalCount;
        }

        public double getSalesTotalCount() {
            return SalesTotalCount;
        }

        public void setSalesTotalCount(double SalesTotalCount) {
            this.SalesTotalCount = SalesTotalCount;
        }

        public double getBrowserTotalCount() {
            return BrowserTotalCount;
        }

        public void setBrowserTotalCount(double BrowserTotalCount) {
            this.BrowserTotalCount = BrowserTotalCount;
        }

        public List<TableSalesInfoBean> getTableSalesInfo() {
            return TableSalesInfo;
        }

        public void setTableSalesInfo(List<TableSalesInfoBean> TableSalesInfo) {
            this.TableSalesInfo = TableSalesInfo;
        }

        public static class TableSalesInfoBean implements Serializable{
            /**
             * PromotionNo : 110
             * OrderPrice : 0
             * BrowserCount : 5
             * OrderCount : 0
             * PromotionName : 测试
             */

            private String PromotionNo;
            private double OrderPrice;
            private int BrowserCount;
            private int OrderCount;
            private String PromotionName;

            public String getPromotionNo() {
                return PromotionNo;
            }

            public void setPromotionNo(String PromotionNo) {
                this.PromotionNo = PromotionNo;
            }

            public double getOrderPrice() {
                return OrderPrice;
            }

            public void setOrderPrice(double OrderPrice) {
                this.OrderPrice = OrderPrice;
            }

            public int getBrowserCount() {
                return BrowserCount;
            }

            public void setBrowserCount(int BrowserCount) {
                this.BrowserCount = BrowserCount;
            }

            public int getOrderCount() {
                return OrderCount;
            }

            public void setOrderCount(int OrderCount) {
                this.OrderCount = OrderCount;
            }

            public String getPromotionName() {
                return PromotionName;
            }

            public void setPromotionName(String PromotionName) {
                this.PromotionName = PromotionName;
            }
        }
    }
}
