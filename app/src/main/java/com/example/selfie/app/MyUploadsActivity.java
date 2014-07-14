package com.example.selfie.app;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;

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

        GENDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_GENDER, "FEMALE");
        TYPE = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_TYPE, "SFW");
        ORDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_ORDER, Order.RANDOMIZED.toString());

        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menu_fragment_my_uploads);
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();

        List<MySelfie> selfieList = dataSource.getAllMySelfies();
        gridView = (GridView) findViewById(R.id.my_uploads_grid_view);
        gridView.setAdapter(new MySelfiesAddapter(selfieList, this, dataSource));
    }

    public void backMySelfies(View v){
        onBackPressed();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment).commit();
        menuVisibility = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

}
