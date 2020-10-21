package com.jizhang.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jizhang.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Intent intent = new Intent();
        //设置跳转页面为登录页
        intent.setClass(SplashActivity.this, LoginActivity.class);

        //延迟一秒钟加载新的窗口
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //开启新的界面
                startActivity(intent);
                //添加渐变的过渡动画
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //结束当前启动页
                finish();
            }
        }, Constant.DELAY_TIME);
    }
}
