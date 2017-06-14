package com.bapm.bzys.newBzys_food.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fs-ljh on 2017/6/5.
 */

public class FoodSale implements Serializable{


    /**
     * TypeSaleCount : 0
     * GoodsTypeId : 39
     * GoodsTypeNmae : 测试菜
     * SalesTotalCount : 0
     * GoodsSales : [{"SaleCount":0,"GoodsName":"。。。"},{"SaleCount":0,"GoodsName":"～～"},{"SaleCount":0,"GoodsName":"aaa"},{"SaleCount":0,"GoodsName":"测测测"},{"SaleCount":0,"GoodsName":"东西"},{"SaleCount":0,"GoodsName":"敢吃吗"},{"SaleCount":0,"GoodsName":"什么"}]
     */
    private int TypeSaleCount;
    private int GoodsTypeId;
    private String GoodsTypeNmae;
    private int SalesTotalCount;
    private List<GoodsSalesBean> GoodsSales;

    public int getTypeSaleCount() {
        return TypeSaleCount;
    }

    public void setTypeSaleCount(int TypeSaleCount) {
        this.TypeSaleCount = TypeSaleCount;
    }

    public int getGoodsTypeId() {
        return GoodsTypeId;
    }

    public void setGoodsTypeId(int GoodsTypeId) {
        this.GoodsTypeId = GoodsTypeId;
    }

    public String getGoodsTypeNmae() {
        return GoodsTypeNmae;
    }

    public void setGoodsTypeNmae(String GoodsTypeNmae) {
        this.GoodsTypeNmae = GoodsTypeNmae;
    }

    public int getSalesTotalCount() {
        return SalesTotalCount;
    }

    public void setSalesTotalCount(int SalesTotalCount) {
        this.SalesTotalCount = SalesTotalCount;
    }

    public List<GoodsSalesBean> getGoodsSales() {
        return GoodsSales;
    }

    public void setGoodsSales(List<GoodsSalesBean> GoodsSales) {
        this.GoodsSales = GoodsSales;
    }

    public static class GoodsSalesBean implements Serializable{
        /**
         * SaleCount : 0
         * GoodsName : 。。。
         */

        private int SaleCount;
        private String GoodsName;

        public int getSaleCount() {
            return SaleCount;
        }

        public void setSaleCount(int SaleCount) {
            this.SaleCount = SaleCount;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String GoodsName) {
            this.GoodsName = GoodsName;
        }
    }
}
