package com.example.selfie.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.selfie.app.fragments.MenuFragment;
import com.example.selfie.utils.MyAccountManager;
import com.example.selfie.utils.MyPreferencesManager;
import com.example.selfie.utils.gallery.ImageLoader;
import com.example.selfie.utils.gallery.NextImageLoader;
import com.example.selfie.utils.gallery.InitialImageLoader;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.data.AddSelfieToFavorites;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.gallery.VoteUp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GalleryActivity extends MyMenuActivity{

    public static final String SELFIE_ID_KAY = "SELFIE_ID";
    public static final String WEB_SERVICE = "http://194.12.246.68/srest";
   /* public static final String WEB_SERVICE = "http://192.168.2.3:8080/RESTfulExample";*/
    public static final String CURRENT_PICTURE_ID = "PICTURE_ID";
    public static final String PICTURES_ID_LIST_KAY = "PICTURES_ID_LIST";

    private boolean menuVisibility = false;

    private SelfieDataSource dataSource;

    private StringBuilder currentPictureId =  new StringBuilder("");

    ImageView imageView;
    TextView scoreTextView;

    int sHeight, sWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        preferencesManager = new MyPreferencesManager(this);

        GENDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_GENDER, "FEMALE");
        TYPE = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_TYPE, "SFW");
        ORDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_ORDER, Order.RANDOMIZED.toString());

        dataSource = new SelfieDataSource(this);
        dataSource.open();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        sHeight = metrics.heightPixels;
        sWidth = metrics.widthPixels;

        imageView = (ImageView) findViewById(R.id.galleryMainImage);
        scoreTextView = (TextView) findViewById(R.id.score_view);
        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menuFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();


        if(savedInstanceState != null && savedInstanceState.containsKey(CURRENT_PICTURE_ID)){
            currentPictureId = new StringBuilder(savedInstanceState.getString(CURRENT_PICTURE_ID));
            new ImageLoader(imageView, scoreTextView, sHeight, sWidth)
                    .execute(WEB_SERVICE, currentPictureId.toString());
        } else {
            new InitialImageLoader(currentPictureId, imageView, scoreTextView, sHeight, sWidth)
                    .execute(WEB_SERVICE, GENDER, TYPE, ORDER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putString(CURRENT_PICTURE_ID, currentPictureId.toString());
    }

    public void onArrowClick(View v){
        String direction = null;
        int viewId = v.getId();
        switch (viewId){
            case R.id.back :
                direction = "UP";
                break;
            case R.id.flowers :
                direction = "DOWN";
                break;
        }
        String picId = currentPictureId.toString();
        new NextImageLoader(imageView, currentPictureId, scoreTextView, sHeight, sWidth)
                .execute(WEB_SERVICE, picId, GENDER, TYPE, direction, ORDER);
    }

    public void onCommentsButtonClick(View v){
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(SELFIE_ID_KAY, currentPictureId.toString());
        startActivity(intent);
    }

    public void onFavoriteButtonClick(View v){
        new AddSelfieToFavorites(imageView, dataSource, getApplicationContext())
                .execute(currentPictureId.toString());
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
        new VoteUp(getApplicationContext(), scoreTextView)
                .execute(WEB_SERVICE, currentPictureId.toString());
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
