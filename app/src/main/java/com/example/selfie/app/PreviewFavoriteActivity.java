package com.example.selfie.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.selfie.app.R;
import com.example.selfie.utils.data.Selfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.favorite.UpdateImageViewInAsync;
import com.example.selfie.utils.file.FileUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class PreviewFavoriteActivity extends Activity {

    private SelfieDataSource dataSource;
    private ImageView imageView;
    private Selfie selfie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_favorite);

        Intent i = getIntent();
        long id = i.getExtras().getLong("id");

        dataSource = new SelfieDataSource(this);
        dataSource.open();

        selfie = dataSource.findById(id);
        imageView = (ImageView) findViewById(R.id.preview);
        new UpdateImageViewInAsync(imageView, this).execute(selfie.getPath());
    }

    public void onDeleteButtonClick(View v){
        dataSource.deleteSelfie(selfie);
        FileUtil.deleteSelfieFiles(selfie, this);
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
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
