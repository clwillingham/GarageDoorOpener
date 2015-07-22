package com.clwillingham.garagedooropener;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor;


public class MainActivity extends Activity {

    Button button;
    LinearLayout lockedLayout;
    SeekBar seekBar;
    Timer timer = new Timer();
    void armButton(final boolean isArmed){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isArmed){
                    button.setEnabled(true);
                    seekBar.setEnabled(false);
                }else{
                    button.setEnabled(false);
                    seekBar.setEnabled(true);
                    seekBar.setProgress(0);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(button == null){
            button = (Button)findViewById(R.id.button);
            seekBar = (SeekBar)findViewById(R.id.seekBar);
            lockedLayout = (LinearLayout)findViewById(R.id.lockedLayout);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    if (seekBar.getProgress() > 95) {
                        armButton(true);
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                armButton(false);
                                this.cancel();
                            }
                        }, 5000);
                    }else{
                        seekBar.setProgress(0);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {


                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    if(progress>95){
                        //seekBar.setThumb(getResources().getDrawable(R.drawable.load_img1));
                    }

                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String msg = "garage door_activate\n";
                                MainApplication.socket.send(new DatagramPacket(msg.getBytes(), msg.length(), new InetSocketAddress("255.255.255.255", 6200)));
                                armButton(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
