package com.example.selfie.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.selfie.app.fragments.MenuFragment;
import com.example.selfie.utils.MyPreferencesManager;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.data.MySelfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.upload.MySelfiesAddapter;

import java.util.List;

public class MyUploadsActivity extends MyMenuActivity {

    private SelfieDataSource dataSource;
    private GridView gridView;
    int sHeight, sWidth;
    List<MySelfie> selfieList = null;
    MySelfiesAddapter mySelfiesAddapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        sHeight = metrics.heightPixels;
        sWidth = metrics.widthPixels;

        preferencesManager = new MyPreferencesManager(this);
        dataSource = new SelfieDataSource(this);
        dataSource.open();

        /*background = (ImageButton) findViewById(R.id.menu_background_my_uploads);

        GENDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_GENDER, "FEMALE");
        TYPE = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_TYPE, "SFW");
        ORDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_ORDER, Order.RANDOMIZED.toString());

        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menu_fragment_my_uploads);
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();*/

        View emptyView = findViewById(R.id.empty_uploads_list);

        selfieList = dataSource.getAllMySelfies();
        gridView = (GridView) findViewById(R.id.my_uploads_grid_view);
        mySelfiesAddapter = new MySelfiesAddapter(selfieList, this, dataSource);
        gridView.setAdapter(mySelfiesAddapter);
        gridView.setEmptyView(emptyView);
    }

    public void backMySelfies(View v){
        onBackPressed();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        /*transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment).commit();
        menuVisibility = false;*/
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    public void removeMySelfieFromDBAndRefreshAdapter(String mySelfieId){
        mySelfiesAddapter.removeItem(mySelfieId);
        dataSource.deleteMySelfie(mySelfieId);
    }

    public void onCameraClickMyUploads(View v){
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }

}
