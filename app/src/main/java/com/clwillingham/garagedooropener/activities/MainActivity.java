package com.clwillingham.garagedooropener.activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.clwillingham.garagedooropener.MainApplication;
import com.clwillingham.garagedooropener.R;
import com.clwillingham.particle_api.models.FunctionResponse;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity {
    Button button;
    LinearLayout lockedLayout;
    SeekBar seekBar;
    Timer timer = new Timer();
    TimerTask lockdownTask;
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

    void cancelLockdown(){
        if(lockdownTask != null){
            lockdownTask.cancel();
            lockdownTask = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
        button = (Button)findViewById(R.id.button);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        lockedLayout = (LinearLayout)findViewById(R.id.lockedLayout);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (seekBar.getProgress() > 95) {
                    armButton(true);
                    cancelLockdown();
                    lockdownTask = new TimerTask() {
                        @Override
                        public void run() {
                            armButton(false);
                            this.cancel();
                        }
                    };
                    long timeout = MainActivity.this.getPrefs().getInt("lockout_timeout", 5)*1000;
                    Log.d("MainActivity", "Timeout is set to " + timeout);
                    timer.schedule(lockdownTask, timeout);

                } else {
                    seekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress > 95) {
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
                        //String msg = "garage door_activate\n";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(false);
                            }
                        });
                        FunctionResponse response = getApp().particle.callFunction("53ff68066667574852302067", "activateDoor");
                        armButton(false);
                        cancelLockdown();
                    }
                }).start();

            }
        });
        if(!getAPI().hasValidAccessToken()){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
        if(!getAPI().hasValidAccessToken()){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    public MainApplication getApp(){
        return (MainApplication) getApplication();
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
