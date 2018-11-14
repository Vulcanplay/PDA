package com.icar.inventory.presenter.impl;

import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;

import java.util.List;

/**
 * Created by light on 2016/3/15.
 */
public interface ImplInventoryPresenter {
    void setInventory(List<PartsDetails> partsList);
    void saveInventory();
    void queryBarcode();
}
