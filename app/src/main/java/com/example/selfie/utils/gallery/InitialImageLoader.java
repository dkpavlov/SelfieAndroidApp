package com.example.selfie.utils.gallery;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.selfie.utils.BitmapAndString;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.http.MyHttpClient;

import java.io.IOException;

/**
 * Created by dpavlov on 15.6.2014 г..
 */
public class InitialImageLoader extends AsyncTask<String, Void, BitmapAndString> {

    private static final String LOG_TAG = "INITIAL_IMAGE_LOADER";
    StringBuilder pictureId;
    ImageView imageView;
    TextView scoreTextView;
    TextView commentCountView;
    TextView favoriteCountView;
    ProgressBar progressBar;

    int sHeight, sWidth;

    public InitialImageLoader(StringBuilder pictureId, ImageView imageView, ProgressBar progressBar,
                              TextView scoreTextView, TextView commentCountView, TextView favoriteCountView,
                              int sHeight, int sWidth) {
        this.pictureId = pictureId;
        this.imageView = imageView;
        this.scoreTextView = scoreTextView;
        this.progressBar = progressBar;
        this.sHeight = sHeight;
        this.sWidth = sWidth;
        this.commentCountView = commentCountView;
        this.favoriteCountView = favoriteCountView;
    }

    protected BitmapAndString doInBackground(String... args) {

        String baseUrl = args[0];
        String gender = args[1];
        String type = args[2];
        Order order = Order.valueOf(args[3]);

        BitmapAndString bitmapAndString = null;

        try{
            switch (order){
                case ORDERED:
                    bitmapAndString = MyHttpClient
                            .getNewestPicture(gender, type, baseUrl, sHeight, sWidth);
                    break;
                case RANDOMIZED:
                    bitmapAndString = MyHttpClient
                            .getNextImage("UP", "0", gender, type, Order.RANDOMIZED, baseUrl, sHeight, sWidth);
                    break;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return bitmapAndString;
    }

    protected void onPostExecute(BitmapAndString result) {
        if(result != null){
            pictureId.replace(0, pictureId.length(), result.getStr());
            imageView.setImageBitmap(result.getBitmap());
            scoreTextView.setText(result.getScore());
            commentCountView.setText(result.getCommentCount());
            favoriteCountView.setText(result.getFavoriteCount());
        }
        progressBar.setVisibility(View.GONE);
    }




}
