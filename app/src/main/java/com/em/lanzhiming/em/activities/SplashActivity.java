package com.em.lanzhiming.em.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.em.lanzhiming.em.MainActivity;
import com.em.lanzhiming.em.R;
import com.em.lanzhiming.em.global.AppConstants;
import com.em.lanzhiming.em.utils.SpUtils;


/**
 *
 */


public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        boolean isRemember = SpUtils.getBoolean(this, AppConstants.REMEMBER);
        // 如果是第一次启动或者退出登录，则先进入登录页
        if (!isFirstOpen||!isRemember) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
