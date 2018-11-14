package com.icar.inventory.model;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by light on 2016/3/6.
 */
public class Employee {
    /***
     * empUserName:用户名
     * empPassword:密码
     * empPower:权限
     * */
    private int id;
    private String userID;
    private String userName;
    private String password;
    private int shopId;
    private int userFlag;
    private String buttonPower;
    private int userType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUserFlag() {
        return userFlag;
    }

    public void setUserFlag(int userFlag) {
        this.userFlag = userFlag;
    }

    public String getButtonPower() {
        return buttonPower;
    }

    public void setButtonPower(String buttonPower) {
        this.buttonPower = buttonPower;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public Employee(){

    }

    //登陆构造函数
    public Employee(String userID,String password){
        this.userID = userID;
        this.password = password;
    }
    public Employee(int id,String userID,String userName,String password,String buttonPower,int shopId,int userFlag,int userType){
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.buttonPower = buttonPower;
        this.shopId = shopId;
        this.userFlag = userFlag;
        this.userType = userType;
    }

    //获取用户信息
    public static SharedPreferences sf;
    public static Employee getEmployee(Activity activity){
        sf = activity.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        return new Employee(sf.getInt("idKey",0),sf.getString("userID",""),sf.getString("userName",""),sf.getString("password",""),sf.getString("buttonPower",""),sf.getInt("shopId",0),sf.getInt("userFlag",0),sf.getInt("userType",0));

    }
}
