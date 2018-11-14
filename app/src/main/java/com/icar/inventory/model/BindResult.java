package com.icar.inventory.model;

/**
 * Created by light on 2016/3/14.
 */
public class BindResult {
    private String result;
    private String message;
    private int total;

    public BindResult(){

    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
