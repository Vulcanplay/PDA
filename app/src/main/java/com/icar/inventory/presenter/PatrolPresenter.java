package com.icar.inventory.presenter;

import android.app.Activity;

import com.icar.inventory.PatrolActivity;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.model.PartsParameters;
import com.icar.inventory.presenter.impl.ImplInventoryPresenter;
import com.icar.inventory.presenter.impl.ImplPatrolPresenter;
import com.icar.inventory.services.InventoryServices;
import com.icar.inventory.services.PatrolServices;

import java.util.List;

/**
 * Created by light on 2016/3/15.
 */
public class PatrolPresenter {
    ImplPatrolPresenter implPatrolPresenter;
    PatrolServices patrolServices;
    PatrolActivity activity;
    Activity mainActivity;

    public PatrolPresenter(ImplPatrolPresenter implPatrolPresenter, PatrolActivity activity){
        this.implPatrolPresenter = implPatrolPresenter;
        patrolServices = new PatrolServices();
        this.activity = activity;
    }
    public PatrolPresenter(ImplPatrolPresenter implPatrolPresenter,Activity mainActivity){
        this.implPatrolPresenter = implPatrolPresenter;
        patrolServices = new PatrolServices();
        this.mainActivity = mainActivity;
    }
    public void setPatrolList(String storeId){
        patrolServices.setImplPatrolPresenter(implPatrolPresenter);
        patrolServices.getPartsForPatrol(activity,storeId);
    }
    public void saveInventory(String id){
        patrolServices.setImplPatrolPresenter(implPatrolPresenter);
        patrolServices.setPartsForPatrol(id,activity);
    }
    public void pQueryBarcode(PartsParameters partsParameters){
        patrolServices.setImplPatrolPresenter(implPatrolPresenter);
        patrolServices.pQueryBarcode(partsParameters,mainActivity);
    }
}
