package com.clwillingham.garagedooropener.activities;

import android.support.v7.app.AppCompatActivity;

import com.clwillingham.garagedooropener.MainApplication;

/**
 * Created by Chris on 7/29/2015.
 */
public class BaseActivity extends AppCompatActivity {
    public MainApplication getApp(){
        return (MainApplication)getApplication();
    }
}
