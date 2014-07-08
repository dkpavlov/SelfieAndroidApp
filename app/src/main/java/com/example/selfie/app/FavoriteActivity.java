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

import com.example.selfie.app.R;
import com.example.selfie.utils.data.Selfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.favorite.ImageAdapter;

import java.util.List;

public class FavoriteActivity extends Activity {

    private SelfieDataSource dataSource;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        dataSource = new SelfieDataSource(this);
        dataSource.open();

        List<Selfie> selfieList = dataSource.getAllSelfis();
        gridView = (GridView) findViewById(R.id.favorite_grid_view);
        gridView.setAdapter(new ImageAdapter(selfieList, this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent i = new Intent(getApplicationContext(), PreviewFavoriteActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        super.onBackPressed();
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
