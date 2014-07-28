package com.example.selfie.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.selfie.app.R;
import com.example.selfie.app.fragments.MenuFragment;
import com.example.selfie.utils.MyPreferencesManager;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.data.Selfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.favorite.ImageAdapter;

import java.util.List;

public class FavoriteActivity extends MyMenuActivity {

    private SelfieDataSource dataSource;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        preferencesManager = new MyPreferencesManager(this);
        dataSource = new SelfieDataSource(this);
        dataSource.open();

        background = (ImageButton) findViewById(R.id.menu_background_favorite);

        GENDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_GENDER, "FEMALE");
        TYPE = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_TYPE, "SFW");
        ORDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_ORDER, Order.RANDOMIZED.toString());

        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menuFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();

        View emptyView = findViewById(R.id.empty_favorite_list);

        List<Selfie> selfieList = dataSource.getAllSelfis();
        gridView = (GridView) findViewById(R.id.favorite_grid_view);
        gridView.setAdapter(new ImageAdapter(selfieList, this, dataSource));
        gridView.setEmptyView(emptyView);
    }

    public void backFavorite(View v){
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
