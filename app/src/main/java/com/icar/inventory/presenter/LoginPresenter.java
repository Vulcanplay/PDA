package com.icar.inventory.presenter;

import android.app.Activity;

import com.icar.inventory.LoginActivity;
import com.icar.inventory.model.Employee;
import com.icar.inventory.presenter.impl.ImplLoginPresenter;
import com.icar.inventory.services.LoginServices;

/**
 * Created by light on 2016/3/6.
 */
public class LoginPresenter {
    ImplLoginPresenter implLoginPresenter;
    LoginServices loginServices;
    LoginActivity activity;

    public LoginPresenter(ImplLoginPresenter implLoginPresenter, LoginActivity activity){
        this.implLoginPresenter = implLoginPresenter;
        loginServices = new LoginServices();
        this.activity = activity;
    }
    public void setLogin(Employee employee){
        implLoginPresenter.logIn(loginServices.checkLogin(employee,activity));
    }
}
