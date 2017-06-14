package com.bapm.bzys.newBzys_food.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by fs-ljh on 2017/6/5.
 */

public class sort implements Comparator<FoodSale.GoodsSalesBean>,Serializable {

    private final String str;

    public sort(String str){
        this.str=str;
    }
    @Override
    public int compare(FoodSale.GoodsSalesBean lhs, FoodSale.GoodsSalesBean rhs) {
             // TODO Auto-generated method stub
        if (str.equals("desc")) {
            return rhs.getSaleCount() - lhs.getSaleCount();
        }
        return lhs.getSaleCount()-rhs.getSaleCount();
         }
}
