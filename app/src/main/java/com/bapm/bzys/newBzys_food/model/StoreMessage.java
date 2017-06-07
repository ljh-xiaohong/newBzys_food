package com.bapm.bzys.newBzys_food.model;

import java.io.Serializable;

/**
 * Created by fs-ljh on 2017/5/26.
 */

public class StoreMessage implements Serializable{

    /**
     * CompanyName : 建洪测试的店铺
     * ProvinceName : 河北
     * CityName : 秦皇岛
     * AreaName : 海港
     * CompanyAddress : 人和镇
     * Name : 建洪
     * TelePhone : 13911111111
     * StandByEmail :
     * ProvinceID : 130000
     * CityID : 130300
     * AreaID : 130302
     * EnterpriseCode : 1000011
     */

    private String CompanyName;
    private String ProvinceName;
    private String CityName;
    private String AreaName;
    private String CompanyAddress;
    private String Name;
    private String TelePhone;
    private String StandByEmail;
    private int ProvinceID;
    private int CityID;
    private int AreaID;
    private String EnterpriseCode;

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String CompanyAddress) {
        this.CompanyAddress = CompanyAddress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getTelePhone() {
        return TelePhone;
    }

    public void setTelePhone(String TelePhone) {
        this.TelePhone = TelePhone;
    }

    public String getStandByEmail() {
        return StandByEmail;
    }

    public void setStandByEmail(String StandByEmail) {
        this.StandByEmail = StandByEmail;
    }

    public int getProvinceID() {
        return ProvinceID;
    }

    public void setProvinceID(int ProvinceID) {
        this.ProvinceID = ProvinceID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int CityID) {
        this.CityID = CityID;
    }

    public int getAreaID() {
        return AreaID;
    }

    public void setAreaID(int AreaID) {
        this.AreaID = AreaID;
    }

    public String getEnterpriseCode() {
        return EnterpriseCode;
    }

    public void setEnterpriseCode(String EnterpriseCode) {
        this.EnterpriseCode = EnterpriseCode;
    }
}
