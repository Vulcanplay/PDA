package com.icar.inventory.model;

import android.app.Activity;

import com.icar.inventory.BindActivity;
import com.zbar.lib.CaptureActivity;

/**
 * Created by light on 2016/3/15.
 */
public class PartsParameters {

    //查询条件
    //1全部 2未绑定 3已绑定
    private int type;
    //店铺id
    private String storeId;
    //查询条件
    private String query;
    //配件ID
    private String orderNo;
    //绑定的配件码
    private String itemCode;
    //数量
    private String total;
    //二维码
    private String barCode;
    //配件名称
    private String supplier;
    //主窗体
    private BindActivity activity;
    private CaptureActivity captureActivity;

    private String scanningId;
    private String scanningCode;
    private String scanningTotal;
    private String itemName;
    public PartsParameters(){

    }
    public PartsParameters(int type, String storeId, String query, BindActivity activity){
        this.type =type;
        this.storeId = storeId;
        this.query = query;
        this.activity = activity;
    }
    public PartsParameters(String orderNo,String storeId,String itemCode, CaptureActivity captureActivity){
        this.orderNo = orderNo;
        this.storeId = storeId;
        this.itemCode = itemCode;
        this.captureActivity = captureActivity;
    }
    public PartsParameters(String orderNo,String barCode,String itemCode, String supplier,String total, String itemName, CaptureActivity captureActivity){
        this.orderNo = orderNo;
        this.barCode = barCode;
        this.supplier = supplier;
        this.total = total;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.captureActivity = captureActivity;
    }
    public PartsParameters(String id, String code, String total){
        this.scanningId = id;
        this.scanningCode = code;
        this.scanningTotal = total;
    }
    public CaptureActivity getCaptureActivity() {
        return captureActivity;
    }

    public void setCaptureActivity(CaptureActivity captureActivity) {
        this.captureActivity = captureActivity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getScanningId() {
        return scanningId;
    }

    public void setScanningId(String scanningId) {
        this.scanningId = scanningId;
    }

    public String getScanningCode() {
        return scanningCode;
    }

    public void setScanningCode(String scanningCode) {
        this.scanningCode = scanningCode;
    }

    public String getScanningTotal() {
        return scanningTotal;
    }

    public void setScanningTotal(String scanningTotal) {
        this.scanningTotal = scanningTotal;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public BindActivity getActivity() {
        return activity;
    }

    public void setActivity(BindActivity activity) {
        this.activity = activity;
    }
}
