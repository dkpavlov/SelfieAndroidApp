package com.example.selfie.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MyProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }

    public void onButtonClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.my_uploads:
                intent = new Intent(this, MyUploadsActivity.class);
                break;
            case R.id.upload:
                intent = new Intent(this, UploadActivity.class);
                break;
        }
        startActivity(intent);
    }

    public void backMyProfile(View v){
        onBackPressed();
    }
}
