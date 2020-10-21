package com.jizhang.application;

import android.app.Application;

import com.jizhang.model.User;

public class AccountApplication extends Application {

    private static AccountApplication sAccountApplication;
    public static User sUser;


    public static AccountApplication getApplication() {
        return sAccountApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sAccountApplication = this;
    }
}
