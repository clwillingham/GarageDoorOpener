package com.clwillingham.garagedooropener;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.clwillingham.particle_api.ParticleIO;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by chris on 7/20/15.
 */
public class MainApplication extends Application {
    public ParticleIO particle;
    public SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        particle = new ParticleIO("15a54699126ccf2896e441c91751b7a68650cb6c");
        preferences = getSharedPreferences("garage_door_prefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }
}
