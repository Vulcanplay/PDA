package com.icar.inventory.model;

/**
 * Created by light on 2016/3/10.
 * 配件详情
 */
public class PartsDetails {

    public PartsDetails(){

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

    //批次
    private String batch;
    //价格
    private String priceWithTax;
    //仓库
    private String warehouse;
    //库位
    private String location;
    //库龄
    private String warehouseAge;
    //供应商类别
    private String supplierType;
    //生产线1
    private String productLine1;
    //生产线2
    private String productLine2;
    //生产线3
    private String productLine3;
    //是否删除
    private int delFlag;
    //系统数量
    private int sysQuantity;
    //配件数量
    private int quantity;
    //数量单位
    private String unit;
    //入库单号
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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


    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPriceWithTax() {
        return priceWithTax;
    }

    public void setPriceWithTax(String priceWithTax) {
        this.priceWithTax = priceWithTax;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWarehouseAge() {
        return warehouseAge;
    }

    public void setWarehouseAge(String warehouseAge) {
        this.warehouseAge = warehouseAge;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getProductLine1() {
        return productLine1;
    }

    public void setProductLine1(String productLine1) {
        this.productLine1 = productLine1;
    }

    public String getProductLine2() {
        return productLine2;
    }

    public void setProductLine2(String productLine2) {
        this.productLine2 = productLine2;
    }

    public String getProductLine3() {
        return productLine3;
    }

    public void setProductLine3(String productLine3) {
        this.productLine3 = productLine3;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
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
}
