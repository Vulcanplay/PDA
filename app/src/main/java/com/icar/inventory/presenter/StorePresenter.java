package com.icar.inventory.presenter;

import android.app.Activity;
import android.util.Log;

import com.icar.inventory.OptionShopActivity;
import com.icar.inventory.model.Store;
import com.icar.inventory.presenter.impl.ImplStorePresenter;
import com.icar.inventory.services.StoreServices;

import java.util.List;

/**
 * Created by light on 2016/3/8.
 */
public class StorePresenter {
    ImplStorePresenter implStorePresenter;
    StoreServices storeServices;
    String query = "";
    OptionShopActivity activity;

    public StorePresenter(ImplStorePresenter implStorePresenter, String query, OptionShopActivity activity){
        this.implStorePresenter = implStorePresenter;
        storeServices = new StoreServices();
        Log.d("Presenter Query",query);
        this.query = query;
        this.activity = activity;
    }

    public void getStore(){
        storeServices.setImplStorePresenter(implStorePresenter);
        storeServices.getStore(query,activity);
        //implStorePresenter.setStore();
    }
}
