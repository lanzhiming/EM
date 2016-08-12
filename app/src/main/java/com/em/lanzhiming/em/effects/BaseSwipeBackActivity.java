package com.em.lanzhiming.em.effects;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.em.lanzhiming.em.R;

/**
 * Created by spiritTalk on 2015/5/5.
 */
public class BaseSwipeBackActivity extends AppCompatActivity {
    SwipeBackLayout swipeBackLayout;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        swipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.swipe_back_layout, null);
        swipeBackLayout.attachToActivity(this);
    }

}
