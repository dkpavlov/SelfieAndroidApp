package com.example.selfie.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.selfie.app.fragments.MenuFragment;
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
import java.util.List;

public class GalleryActivity extends MyMenuActivity{

    public static final String SELFIE_ID_KAY = "SELFIE_ID";
    public static final String WEB_SERVICE = "http://194.12.246.68/srest";
    public static final String CURRENT_PICTURE_ID = "PICTURE_ID";
    public static final String PICTURES_ID_LIST_KAY = "PICTURES_ID_LIST";

    private boolean menuVisibility = false;

    private SelfieDataSource dataSource;

    private StringBuilder currentPictureId =  new StringBuilder("");
    private List<String> oldIds = new ArrayList<String>();
    private int currentIndexInList = 0;
    private boolean inList = false;

    ImageView imageView;
    TextView scoreTextView;
    TextView commentCountView;
    TextView favoriteCountView;
    ProgressBar progressBar;
    ImageButton hintsButton;

    int sHeight, sWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        preferencesManager = new MyPreferencesManager(this);

        GENDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_GENDER, "FEMALE");
        TYPE = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_TYPE, "SFW");
        ORDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_ORDER, Order.RANDOMIZED.toString());

        String hints = preferencesManager.getPreferences(MyPreferencesManager.GALLERY_HINTS, "true");

        dataSource = new SelfieDataSource(this);
        dataSource.open();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        sHeight = metrics.heightPixels;
        sWidth = metrics.widthPixels;

        imageView = (ImageView) findViewById(R.id.galleryMainImage);
        scoreTextView = (TextView) findViewById(R.id.score_view);
        commentCountView = (TextView) findViewById(R.id.comment_count_view);
        favoriteCountView = (TextView) findViewById(R.id.favorite_count_view);
        progressBar = (ProgressBar) findViewById(R.id.galleryProgressBar);
        background = (ImageButton) findViewById(R.id.menu_background);
        hintsButton = (ImageButton) findViewById(R.id.hints);

        if(hints.equals("true")){
            hintsButton.setVisibility(View.VISIBLE);
        }

        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menuFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();


        if(savedInstanceState != null && savedInstanceState.containsKey(CURRENT_PICTURE_ID)){
            currentPictureId = new StringBuilder(savedInstanceState.getString(CURRENT_PICTURE_ID));
            new ImageLoader(imageView, scoreTextView, commentCountView, favoriteCountView,
                            progressBar, sHeight, sWidth)
                    .execute(WEB_SERVICE, currentPictureId.toString());
        } else {
            new InitialImageLoader(currentPictureId, imageView, progressBar,
                                   scoreTextView, commentCountView, favoriteCountView,
                                   sHeight, sWidth)
                    .execute(WEB_SERVICE, GENDER, TYPE, ORDER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putString(CURRENT_PICTURE_ID, currentPictureId.toString());
    }

    private void loadNextImage(int direction){
        if(direction == 1){
            if(!inList){
                if(oldIds.isEmpty() || !currentPictureId.toString().equals(oldIds.get(oldIds.size() - 1))){
                    oldIds.add(currentPictureId.toString());
                    currentIndexInList++;
                }
                new NextImageLoader(imageView, currentPictureId,
                                    scoreTextView, commentCountView, favoriteCountView,
                                    progressBar, sHeight, sWidth)
                        .execute(WEB_SERVICE, currentPictureId.toString(), GENDER, TYPE, "UP", ORDER);
            } else {
                currentIndexInList++;
                currentPictureId = new StringBuilder(oldIds.get(currentIndexInList));
                new ImageLoader(imageView, scoreTextView, commentCountView, favoriteCountView,
                        progressBar, sHeight, sWidth)
                        .execute(WEB_SERVICE, currentPictureId.toString());
            }
        } else {
            currentIndexInList--;
            if(currentIndexInList < 0){
                currentIndexInList = 0;
                progressBar.setVisibility(View.GONE);
            } else {
                currentPictureId = new StringBuilder(oldIds.get(currentIndexInList));
                new ImageLoader(imageView, scoreTextView, commentCountView, favoriteCountView,
                        progressBar, sHeight, sWidth)
                        .execute(WEB_SERVICE, currentPictureId.toString());
            }
        }
    }

    public void onArrowClick(View v){
        progressBar.setVisibility(View.VISIBLE);
        int direction = 0;
        switch (v.getId()){
            case R.id.back:
                direction = -1;
                inList = true;
                break;
            case R.id.flowers:
                direction = 1;
                if(currentIndexInList >= (oldIds.size() - 1)){
                    inList = false;
                }
                break;
        }
        loadNextImage(direction);
        /*
        String direction = null;
        int viewId = v.getId();
        String nextId = null;
        switch (viewId){
            case R.id.back :
                int dir = -1;
                if(!inList){
                    oldIds.add(currentPictureId.toString());
                    currentIndexInList++;
                    dir = -2;
                }
                inList = true;
                nextId = imageIdFromList(dir);
                if(nextId != null){
                    currentPictureId.replace(0, currentPictureId.length(), nextId);
                    new ImageLoader(imageView, scoreTextView, commentCountView, favoriteCountView,
                                    progressBar, sHeight, sWidth)
                            .execute(WEB_SERVICE, nextId);
                } else {
                    progressBar.setVisibility(View.GONE);
                    currentIndexInList--;
                }
                break;
            case R.id.flowers :
                if((currentIndexInList + 1) == (oldIds.size())){
                    inList = false;
                    currentIndexInList++;
                }
                if(inList){
                    nextId = imageIdFromList(1);
                    if(nextId == null){
                        currentPictureId = new StringBuilder(oldIds.get(0));
                    } else {
                        currentPictureId = new StringBuilder(nextId);
                    }
                    new ImageLoader(imageView, scoreTextView, commentCountView, favoriteCountView,
                                    progressBar, sHeight, sWidth)
                            .execute(WEB_SERVICE, nextId);
                } else {
                    String picId = currentPictureId.toString();
                    if(oldIds.isEmpty() || !oldIds.get(oldIds.size() - 1).equals(picId)){
                        oldIds.add(picId);
                        currentIndexInList++;
                    }
                    new NextImageLoader(imageView, currentPictureId, scoreTextView, commentCountView, favoriteCountView,
                                        progressBar, sHeight, sWidth)
                            .execute(WEB_SERVICE, picId, GENDER, TYPE, "UP", ORDER);
                }
                break;
        }*/
    }

    public void onCommentsButtonClick(View v){
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(SELFIE_ID_KAY, currentPictureId.toString());
        startActivity(intent);
    }

    public void onFavoriteButtonClick(View v){
        new AddSelfieToFavorites(imageView, favoriteCountView, dataSource, getApplicationContext())
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

    public void onHintClick(View v){
        hintsButton.setVisibility(View.GONE);
        preferencesManager.setPreferences(MyPreferencesManager.GALLERY_HINTS, "false");
    }

    public void onCameraClick(View v){
        Intent intent = new Intent(this, UploadActivity.class);
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

    private String imageIdFromList(int dir){
        int nextCursor = currentIndexInList + dir;
        if(oldIds.isEmpty() || nextCursor < 0){
            return  null;
        }
        currentIndexInList = nextCursor;
        return oldIds.get(nextCursor);
    }


}
