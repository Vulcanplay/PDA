package com.icar.inventory.model;

/**
 * Created by light on 2016/3/10.
 */
public class Parts {
    public Parts(){

    }
    private int id;
    //配件编码
    private String itemCode;
    //配件名称
    private String itemName;
    //供应商
    private String supplier;
    //是否绑定 0未绑定 1已绑定
    private int bindFlag;
    //条形码
    private String barcode;
    //盘点标示 0未盘点 1未盘到 2已盘点
    private int inventoryFlag;
    //父级id
    private int masterId;
    //系统数量
    private int sysQuantity;
    //配件数量
    private int quantity;
    //数量单位
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getBindFlag() {
        return bindFlag;
    }

    public void setBindFlag(int bindFlag) {
        this.bindFlag = bindFlag;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getInventoryFlag() {
        return inventoryFlag;
    }

    public void setInventoryFlag(int inventoryFlag) {
        this.inventoryFlag = inventoryFlag;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getSysQuantity() {
        return sysQuantity;
    }

    public void setSysQuantity(int sysQuantity) {
        this.sysQuantity = sysQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

