package com.bapm.bzys.newBzys_food.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fs-ljh on 2017/4/14.
 */

public class Order implements Serializable{


    /**
     * cofds : [{"Headimgurl":"http://wx.qlogo.cn/mmopen/ib9xz9liaSKvuNWMicOvMqjEE3ibWWMuAgYL7ovQ6SYdBEWibuvByUvUUribZ0dSKNYyaE7kUmyhLplU482Mnhx4dibWJDSZy2fkyjg/0","OrderDetails":[{"PicThum":"https://tqnimg.bzys.cn/FnQhIR9v2QfHzc9Qd5HNI_HMNExJ?imageView/1/w/160/h/160/q/90","GoodsCountedBy":"","ConsumerId":3,"Number":1,"GoodsNo":"6","Need":null,"ConsumerNickName":"萧萧","GoodsName":"aaa","OrderFormDetailsStatus":7,"GoodsSalesFlag":1,"Price":666,"OrderFormID":6,"ID":12,"Headimgurl":"http://wx.qlogo.cn/mmopen/ib9xz9liaSKvuNWMicOvMqjEE3ibWWMuAgYL7ovQ6SYdBEWibuvByUvUUribZ0dSKNYyaE7kUmyhLplU482Mnhx4dibWJDSZy2fkyjg/0"}],"ConsumerNickName":"萧萧"}]
     * PromotionID : 1047
     * OrderFormStatus : 9
     * RequieredGoods : [{"PicThum":"https://tqnimg.bzys.cn/Ft8yIJBCkK0dYzvqFrLod84IS1Ne?imageView/1/w/160/h/160/q/90","GoodsCountedBy":"位","GoodsSalesFlag":1,"Number":1,"GoodsNo":"2","GoodsID":65,"Price":1,"GoodsName":"必选"}]
     * Prices : 666
     * TConsumer : 1
     * OrderNumber : 1706011000011102
     * CreateTime : 2017-06-01 12:22:51
     * ID : 6
     * Remarks : null
     * Numbers : 1
     * NumericalOrder : 102
     * ConsumerNickName : 萧**
     */

    private int PromotionID;
    private String OrderFormStatus;
    private Double Prices;
    private int TConsumer;
    private String OrderNumber;
    private String CreateTime;
    private int ID;
    private String Remarks;
    private int Numbers;
    private int NumericalOrder;
    private String ConsumerNickName;
    private List<CofdsBean> cofds;
    private List<RequieredGoodsBean> RequieredGoods;

    public int getPromotionID() {
        return PromotionID;
    }

    public void setPromotionID(int PromotionID) {
        this.PromotionID = PromotionID;
    }

    public String getOrderFormStatus() {
        return OrderFormStatus;
    }

    public void setOrderFormStatus(String OrderFormStatus) {
        this.OrderFormStatus = OrderFormStatus;
    }

    public Double getPrices() {
        return Prices;
    }

    public void setPrices(Double Prices) {
        this.Prices = Prices;
    }

    public int getTConsumer() {
        return TConsumer;
    }

    public void setTConsumer(int TConsumer) {
        this.TConsumer = TConsumer;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String OrderNumber) {
        this.OrderNumber = OrderNumber;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public int getNumbers() {
        return Numbers;
    }

    public void setNumbers(int Numbers) {
        this.Numbers = Numbers;
    }

    public int getNumericalOrder() {
        return NumericalOrder;
    }

    public void setNumericalOrder(int NumericalOrder) {
        this.NumericalOrder = NumericalOrder;
    }

    public String getConsumerNickName() {
        return ConsumerNickName;
    }

    public void setConsumerNickName(String ConsumerNickName) {
        this.ConsumerNickName = ConsumerNickName;
    }

    public List<CofdsBean> getCofds() {
        return cofds;
    }

    public void setCofds(List<CofdsBean> cofds) {
        this.cofds = cofds;
    }

    public List<RequieredGoodsBean> getRequieredGoods() {
        return RequieredGoods;
    }

    public void setRequieredGoods(List<RequieredGoodsBean> RequieredGoods) {
        this.RequieredGoods = RequieredGoods;
    }

    public static class CofdsBean implements Serializable{
        /**
         * Headimgurl : http://wx.qlogo.cn/mmopen/ib9xz9liaSKvuNWMicOvMqjEE3ibWWMuAgYL7ovQ6SYdBEWibuvByUvUUribZ0dSKNYyaE7kUmyhLplU482Mnhx4dibWJDSZy2fkyjg/0
         * OrderDetails : [{"PicThum":"https://tqnimg.bzys.cn/FnQhIR9v2QfHzc9Qd5HNI_HMNExJ?imageView/1/w/160/h/160/q/90","GoodsCountedBy":"","ConsumerId":3,"Number":1,"GoodsNo":"6","Need":null,"ConsumerNickName":"萧萧","GoodsName":"aaa","OrderFormDetailsStatus":7,"GoodsSalesFlag":1,"Price":666,"OrderFormID":6,"ID":12,"Headimgurl":"http://wx.qlogo.cn/mmopen/ib9xz9liaSKvuNWMicOvMqjEE3ibWWMuAgYL7ovQ6SYdBEWibuvByUvUUribZ0dSKNYyaE7kUmyhLplU482Mnhx4dibWJDSZy2fkyjg/0"}]
         * ConsumerNickName : 萧萧
         */

        private String Headimgurl;
        private String ConsumerNickName;
        private List<OrderDetailsBean> OrderDetails;

        public String getHeadimgurl() {
            return Headimgurl;
        }

        public void setHeadimgurl(String Headimgurl) {
            this.Headimgurl = Headimgurl;
        }

        public String getConsumerNickName() {
            return ConsumerNickName;
        }

        public void setConsumerNickName(String ConsumerNickName) {
            this.ConsumerNickName = ConsumerNickName;
        }

        public List<OrderDetailsBean> getOrderDetails() {
            return OrderDetails;
        }

        public void setOrderDetails(List<OrderDetailsBean> OrderDetails) {
            this.OrderDetails = OrderDetails;
        }

        public static class OrderDetailsBean implements Serializable{
            /**
             * PicThum : https://tqnimg.bzys.cn/FnQhIR9v2QfHzc9Qd5HNI_HMNExJ?imageView/1/w/160/h/160/q/90
             * GoodsCountedBy :
             * ConsumerId : 3
             * Number : 1
             * GoodsNo : 6
             * Need : null
             * ConsumerNickName : 萧萧
             * GoodsName : aaa
             * OrderFormDetailsStatus : 7
             * GoodsSalesFlag : 1
             * Price : 666
             * OrderFormID : 6
             * ID : 12
             * Headimgurl : http://wx.qlogo.cn/mmopen/ib9xz9liaSKvuNWMicOvMqjEE3ibWWMuAgYL7ovQ6SYdBEWibuvByUvUUribZ0dSKNYyaE7kUmyhLplU482Mnhx4dibWJDSZy2fkyjg/0
             */

            private String PicThum;
            private String GoodsCountedBy;
            private int ConsumerId;
            private int Number;
            private String GoodsNo;
            private String Need;
            private String ConsumerNickName;
            private String GoodsName;
            private String OrderFormDetailsStatus;
            private String GoodsSalesFlag;
            private Double Price;
            private int OrderFormID;
            private int ID;
            private String Headimgurl;

            public String getPicThum() {
                return PicThum;
            }

            public void setPicThum(String PicThum) {
                this.PicThum = PicThum;
            }

            public String getGoodsCountedBy() {
                return GoodsCountedBy;
            }

            public void setGoodsCountedBy(String GoodsCountedBy) {
                this.GoodsCountedBy = GoodsCountedBy;
            }

            public int getConsumerId() {
                return ConsumerId;
            }

            public void setConsumerId(int ConsumerId) {
                this.ConsumerId = ConsumerId;
            }

            public int getNumber() {
                return Number;
            }

            public void setNumber(int Number) {
                this.Number = Number;
            }

            public String getGoodsNo() {
                return GoodsNo;
            }

            public void setGoodsNo(String GoodsNo) {
                this.GoodsNo = GoodsNo;
            }

            public String getNeed() {
                return Need;
            }

            public void setNeed(String Need) {
                this.Need = Need;
            }

            public String getConsumerNickName() {
                return ConsumerNickName;
            }

            public void setConsumerNickName(String ConsumerNickName) {
                this.ConsumerNickName = ConsumerNickName;
            }

            public String getGoodsName() {
                return GoodsName;
            }

            public void setGoodsName(String GoodsName) {
                this.GoodsName = GoodsName;
            }

            public String getOrderFormDetailsStatus() {
                return OrderFormDetailsStatus;
            }

            public void setOrderFormDetailsStatus(String OrderFormDetailsStatus) {
                this.OrderFormDetailsStatus = OrderFormDetailsStatus;
            }

            public String getGoodsSalesFlag() {
                return GoodsSalesFlag;
            }

            public void setGoodsSalesFlag(String GoodsSalesFlag) {
                this.GoodsSalesFlag = GoodsSalesFlag;
            }

            public Double getPrice() {
                return Price;
            }

            public void setPrice(Double Price) {
                this.Price = Price;
            }

            public int getOrderFormID() {
                return OrderFormID;
            }

            public void setOrderFormID(int OrderFormID) {
                this.OrderFormID = OrderFormID;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getHeadimgurl() {
                return Headimgurl;
            }

            public void setHeadimgurl(String Headimgurl) {
                this.Headimgurl = Headimgurl;
            }
        }
    }

    public static class RequieredGoodsBean implements Serializable{
        /**
         * PicThum : https://tqnimg.bzys.cn/Ft8yIJBCkK0dYzvqFrLod84IS1Ne?imageView/1/w/160/h/160/q/90
         * GoodsCountedBy : 位
         * GoodsSalesFlag : 1
         * Number : 1
         * GoodsNo : 2
         * GoodsID : 65
         * Price : 1
         * GoodsName : 必选
         */

        private String PicThum;
        private String GoodsCountedBy;
        private int GoodsSalesFlag;
        private int Number;
        private String GoodsNo;
        private int GoodsID;
        private Double Price;
        private String GoodsName;

        public String getPicThum() {
            return PicThum;
        }

        public void setPicThum(String PicThum) {
            this.PicThum = PicThum;
        }

        public String getGoodsCountedBy() {
            return GoodsCountedBy;
        }

        public void setGoodsCountedBy(String GoodsCountedBy) {
            this.GoodsCountedBy = GoodsCountedBy;
        }

        public int getGoodsSalesFlag() {
            return GoodsSalesFlag;
        }

        public void setGoodsSalesFlag(int GoodsSalesFlag) {
            this.GoodsSalesFlag = GoodsSalesFlag;
        }

        public int getNumber() {
            return Number;
        }

        public void setNumber(int Number) {
            this.Number = Number;
        }

        public String getGoodsNo() {
            return GoodsNo;
        }

        public void setGoodsNo(String GoodsNo) {
            this.GoodsNo = GoodsNo;
        }

        public int getGoodsID() {
            return GoodsID;
        }

        public void setGoodsID(int GoodsID) {
            this.GoodsID = GoodsID;
        }

        public Double getPrice() {
            return Price;
        }

        public void setPrice(Double Price) {
            this.Price = Price;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String GoodsName) {
            this.GoodsName = GoodsName;
        }
    }
}
