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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.selfie.app.fragments.MenuFragment;
import com.example.selfie.utils.gallery.ImageLoader;
import com.example.selfie.utils.gallery.NextImageLoader;
import com.example.selfie.utils.gallery.InitialImageLoader;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.data.AddSelfieToFavorites;
import com.example.selfie.utils.data.SelfieDataSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GalleryActivity extends Activity implements MenuFragment.OnFragmentInteractionListener{

    public static final String SELFIE_ID_KAY = "SELFIE_ID";
    public static final String WEB_SERVICE = "http://194.12.246.68/srest";
    public static final String CURRENT_PICTURE_ID = "PICTURE_ID";
    public static final String PICTURES_ID_LIST_KAY = "PICTURES_ID_LIST";

    private boolean menuVisibility = false;
    private ArrayList<String> randomPicturesIdStringArray = new ArrayList<String>();

    private SelfieDataSource dataSource;
    private String GENDER;
    private String TYPE;
    private String ORDER;

    private StringBuffer currentPictureId = new StringBuffer("");

    ImageView imageView;
    MenuFragment menuFragment;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        GENDER = intent.getStringExtra(HomeActivity.PICTURES_TYPE_KEY);
        TYPE = intent.getStringExtra(HomeActivity.TYPE_OF_CONTENT_KEY);
        ORDER = intent.getStringExtra(HomeActivity.ORDER_TYPE_KEY);

        dataSource = new SelfieDataSource(this);
        dataSource.open();

        imageView = (ImageView) findViewById(R.id.galleryMainImage);
        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menuFragment);
        menuFragment.getView().setBackgroundColor(Color.parseColor("#222219"));
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();


        if(savedInstanceState != null && savedInstanceState.containsKey(CURRENT_PICTURE_ID)){
            currentPictureId = new StringBuffer(savedInstanceState.getInt(CURRENT_PICTURE_ID));
            randomPicturesIdStringArray = savedInstanceState.getStringArrayList(PICTURES_ID_LIST_KAY);
            GENDER = savedInstanceState.getString(HomeActivity.PICTURES_TYPE_KEY);
            TYPE = savedInstanceState.getString(HomeActivity.TYPE_OF_CONTENT_KEY);
            ORDER = savedInstanceState.getString(HomeActivity.ORDER_TYPE_KEY);
            new ImageLoader(imageView)
                    .execute(WEB_SERVICE, currentPictureId.toString());
        } else {
            new InitialImageLoader(currentPictureId, imageView)
                    .execute(WEB_SERVICE, GENDER, TYPE, ORDER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putString(CURRENT_PICTURE_ID, currentPictureId.toString());
        icicle.putStringArrayList(PICTURES_ID_LIST_KAY, randomPicturesIdStringArray);
        icicle.putString(HomeActivity.PICTURES_TYPE_KEY, GENDER);
        icicle.putString(HomeActivity.TYPE_OF_CONTENT_KEY, TYPE);
        icicle.putString(HomeActivity.ORDER_TYPE_KEY, ORDER);
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
        new NextImageLoader(imageView, currentPictureId)
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

    public void onMenuButtonClick(View v){
        transaction = fragmentManager.beginTransaction();
        if(menuVisibility){
            transaction.hide(menuFragment).commit();
        } else {
            transaction.show(menuFragment).commit();
        }
        menuVisibility = !menuVisibility;
    }

    /* Fragment Buttons */

    public void onGenderButtonClick(View v){
        Intent intent = new Intent(this, GalleryActivity.class);
        if(GENDER.equals("MALE")){
            intent.putExtra(HomeActivity.PICTURES_TYPE_KEY, "FEMALE");
        } else {
            intent.putExtra(HomeActivity.PICTURES_TYPE_KEY, "MALE");
        }
        intent.putExtra(HomeActivity.TYPE_OF_CONTENT_KEY, TYPE);
        intent.putExtra(HomeActivity.ORDER_TYPE_KEY, ORDER);
        startActivity(intent);
    }

    public void nsfwButtonClick(View v){
        Intent intent = new Intent(this, GalleryActivity.class);
        if(TYPE.equals(HomeActivity.NSFW)){
            intent.putExtra(HomeActivity.TYPE_OF_CONTENT_KEY, HomeActivity.SFW);
        } else {
            intent.putExtra(HomeActivity.TYPE_OF_CONTENT_KEY, HomeActivity.NSFW);
        }
        intent.putExtra(HomeActivity.PICTURES_TYPE_KEY, GENDER);
        intent.putExtra(HomeActivity.ORDER_TYPE_KEY, ORDER);
        startActivity(intent);
    }

    public void orderButtonClick(View v){
        Intent intent = new Intent(this, GalleryActivity.class);
        if(ORDER.equals(Order.ORDERED)){
            intent.putExtra(HomeActivity.ORDER_TYPE_KEY, Order.RANDOMIZED.toString());
        } else {
            intent.putExtra(HomeActivity.ORDER_TYPE_KEY, Order.ORDERED.toString());
        }
        intent.putExtra(HomeActivity.PICTURES_TYPE_KEY, GENDER);
        intent.putExtra(HomeActivity.TYPE_OF_CONTENT_KEY, TYPE);
        startActivity(intent);
    }

    public void openFavoritesButtonClick(View v){
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setFragmentButtonTest(){
        Button genderButton = (Button) findViewById(R.id.gender);
        Button nsfwButton = (Button) findViewById(R.id.nsfw);
        Button orderButton = (Button) findViewById(R.id.get_new);

        if(GENDER.equals("MALE")){
            genderButton.setText("Switch to female");
        } else {
            genderButton.setText("Switch to male");
        }

        if(TYPE.equals(HomeActivity.NSFW)){
            nsfwButton.setText("Switch to SFW");
        } else {
            nsfwButton.setText("Switch to NSFW");
        }

        if(ORDER.equals(Order.ORDERED.toString())){
            orderButton.setText("Randomise");
        } else {
            orderButton.setText("Get new");
        }
    }
}
