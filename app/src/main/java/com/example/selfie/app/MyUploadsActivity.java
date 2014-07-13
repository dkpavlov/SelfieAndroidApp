package com.example.selfie.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.selfie.app.R;
import com.example.selfie.utils.data.MySelfie;
import com.example.selfie.utils.data.Selfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.favorite.ImageAdapter;
import com.example.selfie.utils.upload.MySelfiesAddapter;

import java.util.List;

public class MyUploadsActivity extends Activity {

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

        dataSource = new SelfieDataSource(this);
        dataSource.open();

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
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

}
