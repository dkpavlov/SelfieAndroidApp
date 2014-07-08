package com.example.selfie.utils.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by dpavlov on 8.7.2014 г..
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private static final String LOG_TAG = "IMAGE_LOADER";

    ImageView imageView;

    public ImageLoader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        String baseUrl = args[0];
        String pictureId = args[1];
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(baseUrl + "/img/img/" + pictureId).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
