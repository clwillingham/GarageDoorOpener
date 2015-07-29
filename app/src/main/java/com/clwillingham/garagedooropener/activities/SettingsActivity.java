package com.clwillingham.garagedooropener.activities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.clwillingham.garagedooropener.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.qrCodeBtn) Button qrCodeBtn;
    @Bind(R.id.loginBtn) Button loginBtn;
    @Bind(R.id.lockoutTimeoutEdTxt) EditText lockoutTimeoutEdTxt;
    @Bind(R.id.accessTokenEdTxt) EditText accessTokenEdTxt;
    @Bind(R.id.qrCodeImgVw) ImageView qrCodeImgVw;

    @OnClick(R.id.qrCodeBtn)
    public void qrCodeBtnClicked(){
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            //intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                accessTokenEdTxt.setText(contents);
            }
        }
    }

    public void loadQRCode(){
        String token = accessTokenEdTxt.getText().toString();
        if((token != null) && (token.length() > 0)){
            Picasso.with(this).load("https://zxing.org/w/chart?cht=qr&chs=350x350&chld=L&choe=UTF-8&chl="+token).into(qrCodeImgVw);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        loadQRCode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SettingsActivity", "leaving settings activity");
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
