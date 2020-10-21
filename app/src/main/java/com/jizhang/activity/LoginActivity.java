package com.jizhang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.jizhang.R;
import com.jizhang.application.AccountApplication;
import com.jizhang.dao.ExpenseCatDao;
import com.jizhang.dao.IncomeCatDao;
import com.jizhang.dao.UserDao;
import com.jizhang.model.User;
import com.jizhang.utils.ToastUtils;

public class LoginActivity  extends BaseActivity{
    private User user;
    private UserDao userDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(View.INVISIBLE);
        userDao = new UserDao(this);
        user = userDao.getCurrentUser("管理员");
        if(user==null){
            user = new User("管理员",null,null);
            userDao.addUser(user);
            User muser = new User("我",null,null);
            userDao.addUser(muser);
            IncomeCatDao incomeCatDao = new IncomeCatDao(LoginActivity.this);
            incomeCatDao.initIncomeCat(muser);
            ExpenseCatDao expenseCatDao = new ExpenseCatDao(LoginActivity.this);
            expenseCatDao.initExpensesCat(muser);
        }

    }

    public String getPassfirst(){
        return user.getPassfirst();
    }

    public String getPasssecond(){
        return user.getPasssecond();
    }

    public void setPassfirst(String pass){
        userDao.updatePassfirst(pass,user.getId());
        user = userDao.getCurrentUser("管理员");
    }

    public void setPasssecond(String pass){
        userDao.updatePasssecond(pass,user.getId());
        user = userDao.getCurrentUser("管理员");
    }

    public void login(){
        if(getPassfirst()!=null && getPasssecond()!=null){
            final Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            AccountApplication.sUser = new UserDao(this).getCurrentUser("我");
            startActivity(intent);
            finish();
        }
        else{
            ToastUtils.showShort(this,"请注册口令密码和图案密码");
        }
    }

    @Override
    protected Activity getSubActivity() {
        return this;
    }

}
