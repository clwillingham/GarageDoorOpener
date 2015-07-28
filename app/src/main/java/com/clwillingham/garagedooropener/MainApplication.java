package com.clwillingham.garagedooropener;

import android.app.Application;
import android.widget.Toast;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by chris on 7/20/15.
 */
public class MainApplication extends Application {
    public static DatagramSocket socket;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            socket = new DatagramSocket(6200);
        } catch (SocketException e) {
            Toast.makeText(getApplicationContext(), "Unable to start socket, please restart app", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTerminate(){
        socket.close();
        super.onTerminate();
    }
}
