package com.icar.inventory.model;

/**
 * Created by light on 2016/3/14.
 */
public class InventoryResult {
    private String result;
    private String message;

    public InventoryResult(){

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
}
