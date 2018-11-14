package com.icar.inventory.presenter;

import android.app.Activity;

import com.icar.inventory.model.PartsParameters;
import com.icar.inventory.presenter.impl.ImplBindPresenter;
import com.icar.inventory.services.BindServices;

/**
 * Created by light on 2016/3/10.
 */
public class BindPresenter {
    ImplBindPresenter implBindPresenter;
    BindServices bindServices;

    public BindPresenter(ImplBindPresenter implBindPresenter){
        this.implBindPresenter = implBindPresenter;
        bindServices = new BindServices();
    }

    public void getParts(PartsParameters partsParameters){
        bindServices.setImplBindPresenter(implBindPresenter);
        bindServices.getParts(partsParameters);
    }

    public void setBind(PartsParameters partsParameters){
        bindServices.setImplBindPresenter(implBindPresenter);
        bindServices.setBind(partsParameters);
    }
}
