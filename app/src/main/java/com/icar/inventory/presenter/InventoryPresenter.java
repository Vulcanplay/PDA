package com.icar.inventory.presenter;

import android.app.Activity;

import com.icar.inventory.InventoryActivity;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.model.PartsParameters;
import com.icar.inventory.presenter.impl.ImplInventoryPresenter;
import com.icar.inventory.services.BindServices;
import com.icar.inventory.services.InventoryServices;

import java.util.List;

/**
 * Created by light on 2016/3/15.
 */
public class InventoryPresenter {
    ImplInventoryPresenter implInventoryPresenter;
    InventoryServices inventoryServices;
    InventoryActivity activity;
    Activity mainActivity;

    public InventoryPresenter(ImplInventoryPresenter implInventoryPresenter,InventoryActivity activity){
        this.implInventoryPresenter = implInventoryPresenter;
        inventoryServices = new InventoryServices();
        this.activity = activity;
    }
    public InventoryPresenter(ImplInventoryPresenter implInventoryPresenter,Activity mainActivity){
        this.implInventoryPresenter = implInventoryPresenter;
        inventoryServices = new InventoryServices();
        this.mainActivity = mainActivity;
    }

    public void setInventoryList(){
        inventoryServices.setImplInventoryPresenter(implInventoryPresenter);
        inventoryServices.getPartsForInventory(activity);
    }

    public void saveInventory(String id){
        inventoryServices.setImplInventoryPresenter(implInventoryPresenter);
        inventoryServices.setPartsForInventory(id,activity);
    }

    public void queryBarcode(PartsParameters partsParameters){
        inventoryServices.setImplInventoryPresenter(implInventoryPresenter);
        inventoryServices.queryBarcode(partsParameters,mainActivity);
    }
}
