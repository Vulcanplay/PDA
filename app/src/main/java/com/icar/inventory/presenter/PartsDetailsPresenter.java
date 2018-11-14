package com.icar.inventory.presenter;

import com.icar.inventory.PartsDetailsActivity;
import com.icar.inventory.presenter.impl.ImplPartsDetailsPresenter;
import com.icar.inventory.services.PartsDetailsServices;

/**
 * Created by light on 2016/3/12.
 */
public class PartsDetailsPresenter {
    ImplPartsDetailsPresenter implPartsDetailsPresenter;
    PartsDetailsServices partsDetailsServices;
    PartsDetailsActivity activity;
    String itemCode;

    public PartsDetailsPresenter(ImplPartsDetailsPresenter implPartsDetailsPresenter,String itemCode,PartsDetailsActivity activity){
        this.implPartsDetailsPresenter = implPartsDetailsPresenter;
        partsDetailsServices = new PartsDetailsServices();
        this.itemCode = itemCode;
        this.activity = activity;
    }

    public void setPartsDetails(){
        partsDetailsServices.setImplPartsDetailsPresenter(implPartsDetailsPresenter);
        partsDetailsServices.getPartsDetails(itemCode,activity);
    }
}
