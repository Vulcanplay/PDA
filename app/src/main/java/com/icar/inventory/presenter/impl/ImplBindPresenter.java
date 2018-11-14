package com.icar.inventory.presenter.impl;

import com.icar.inventory.model.BindResult;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;

import java.util.List;

/**
 * Created by light on 2016/3/10.
 */
public interface ImplBindPresenter {
    void setParts(List<PartsDetails> partsList);
    void setBind(BindResult bindResult);
}
