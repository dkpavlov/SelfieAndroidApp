package com.example.selfie.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.selfie.app.fragments.MenuFragment;
import com.example.selfie.utils.MyPreferencesManager;
import com.example.selfie.utils.Order;

public class MyProfileActivity extends MyMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        preferencesManager = new MyPreferencesManager(this);

        background = (ImageButton) findViewById(R.id.menu_background_my_profile);

        GENDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_GENDER, "FEMALE");
        TYPE = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_TYPE, "SFW");
        ORDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_ORDER, Order.RANDOMIZED.toString());

        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menuFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();
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

    @Override
    protected void onResume() {
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment).commit();
        menuVisibility = false;
        super.onResume();
    }

    public void backMyProfile(View v){
        onBackPressed();
    }
}
