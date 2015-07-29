package com.clwillingham.garagedooropener.activities;

import android.content.SharedPreferences;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;

import com.clwillingham.garagedooropener.MainApplication;
import com.clwillingham.particle_api.ParticleIO;

/**
 * Created by Chris on 7/29/2015.
 */
public class BaseActivity extends AppCompatActivity {
    public MainApplication getApp(){
        return (MainApplication)getApplication();
    }

    public ParticleIO getAPI(){
        return getApp().particle;
    }

    public SharedPreferences getPrefs(){
        return getApp().preferences;
    }
}
