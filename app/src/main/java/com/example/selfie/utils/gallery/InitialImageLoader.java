package com.example.selfie.utils.gallery;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.selfie.utils.BitmapAndString;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.http.MyHttpClient;

import java.io.IOException;

/**
 * Created by dpavlov on 15.6.2014 г..
 */
public class InitialImageLoader extends AsyncTask<String, Void, BitmapAndString> {

    private static final String LOG_TAG = "INITIAL_IMAGE_LOADER";
    StringBuffer pictureId;
    ImageView imageView;

    public InitialImageLoader(StringBuffer pictureId, ImageView imageView) {
        this.pictureId = pictureId;
        this.imageView = imageView;
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
                            .getNewestPicture(gender, type, baseUrl);
                    break;
                case RANDOMIZED:
                    bitmapAndString = MyHttpClient
                            .getNextImage("UP", "0", gender, type, Order.RANDOMIZED, baseUrl);
                    break;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }


        return bitmapAndString;
    }

    protected void onPostExecute(BitmapAndString result) {
        pictureId.delete(0, pictureId.length());
        pictureId.append(result.getStr());
        imageView.setImageBitmap(result.getBitmap());
    }




}
