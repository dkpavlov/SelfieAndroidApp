package com.example.selfie.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.selfie.app.R;
import com.example.selfie.utils.data.AddSelfieToFavorites;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.gallery.ImageLoader;
import com.example.selfie.utils.gallery.VoteUp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PreviewMyUploadActivity extends Activity {


    ImageView imageView;
    TextView scoreView, commentView, favoriteView;
    ProgressBar progressBar;

    String id = null;
    SelfieDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_my_upload);

        imageView = (ImageView) findViewById(R.id.my_uploads_preview_image_view);
        scoreView = (TextView) findViewById(R.id.my_uplode_score_view);
        commentView = (TextView) findViewById(R.id.my_uplode_comment_count_view);
        favoriteView = (TextView) findViewById(R.id.my_uplode_favorites_count_view);
        progressBar = (ProgressBar) findViewById(R.id.preview_my_upload_progress_bar);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int sHeight = metrics.heightPixels;
        int sWidth = metrics.widthPixels;

        dataSource = new SelfieDataSource(this);

        new ImageLoader(imageView, scoreView, commentView, favoriteView, progressBar, sHeight, sWidth)
                .execute(GalleryActivity.WEB_SERVICE, id);
    }

    public void onCommentsButtonClick(View v){
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(GalleryActivity.SELFIE_ID_KAY, id);
        startActivity(intent);
    }

    public void onFavoriteButtonClick(View v){
        new AddSelfieToFavorites(imageView, favoriteView, dataSource, getApplicationContext())
                .execute(id);
    }

    public void onShareButtonClick(View v){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(intent, "Share Image"));

    }

    public void onVoteUpButtonClick(View v){
        new VoteUp(getApplicationContext(), scoreView)
                .execute(GalleryActivity.WEB_SERVICE, id);
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
