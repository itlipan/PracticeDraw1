package com.souche.fengche.viewdemo.gson;

import java.util.List;

/**
 * Created by xf on 16/7/11
 */
public class CustomerBaseInfo {

    private String address;
    private String alipayAccount;
    private String backupPhone;
    private String belongSales;
    private String belongSalesName;
    private long birthday;
    private String city;
    private String cityName;
    private String creator;
    private String crmUserId;
    private long dateCreate;
    private long dateUpdate;
    private long dateDelete;
    private String id;
    private String identificationNumber;
    private int isArriveShop;
    private int lastestFollowTime;
    private String level;
    private String levelName;
    private String name;
    private String operator;
    private String operatorName;
    private String phone;
    private List<String> pictures;
    private String province;
    private String provinceName;
    private String district;
    private String districtName;
    private String qq;
    private String remark;
    private int sellerLable; // 卖家标签 1有
    private int sequence;
    private int sex; // 性别 0男 1女
    private String sexName;
    private String shopCode;
    private String source;
    private String sourceName;
    private String shopName;
    private String taobaoAccount;
    private String wechat;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getTaobaoAccount() {
        return taobaoAccount;
    }

    public void setTaobaoAccount(String taobaoAccount) {
        this.taobaoAccount = taobaoAccount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCrmUserId() {
        return crmUserId;
    }

    public void setCrmUserId(String crmUserId) {
        this.crmUserId = crmUserId;
    }

    public void setDateCreate(long dateCreate) {
        this.dateCreate = dateCreate;
    }

    public long getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(int dateCreate) {
        this.dateCreate = dateCreate;
    }

    public long getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(long dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public long getDateDelete() {
        return dateDelete;
    }

    public void setDateDelete(long dateDelete) {
        this.dateDelete = dateDelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBackupPhone() {
        return backupPhone;
    }

    public void setBackupPhone(String backupPhone) {
        this.backupPhone = backupPhone;
    }

    public String getQQ() {
        return qq;
    }

    public void setQQ(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBelongSales() {
        return belongSales;
    }

    public void setBelongSales(String belongSales) {
        this.belongSales = belongSales;
    }

    public int getSellerLable() {
        return sellerLable;
    }

    public void setSellerLable(int sellerLable) {
        this.sellerLable = sellerLable;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getBelongSalesName() {
        return belongSalesName;
    }

    public void setBelongSalesName(String belongSalesName) {
        this.belongSalesName = belongSalesName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        if (cityName == null) {
            return "";
        }
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getProvinceName() {
        if (provinceName == null) {
            return "";
        }
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
