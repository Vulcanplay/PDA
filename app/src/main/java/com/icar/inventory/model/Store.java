package com.icar.inventory.model;

/**
 * Created by light on 2016/3/8.
 */
public class Store {
    private int shopId;
    private String shopName;

    public Store(){

    }
    public Store(int shopId,String shopName){
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
