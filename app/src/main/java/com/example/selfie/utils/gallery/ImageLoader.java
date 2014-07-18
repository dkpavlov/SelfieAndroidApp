package com.example.selfie.utils.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.selfie.utils.BitmapAndString;
import com.example.selfie.utils.http.MyHttpClient;
import com.example.selfie.utils.ui.ScaleBitmap;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dpavlov on 8.7.2014 г..
 */
public class ImageLoader extends AsyncTask<String, Void, BitmapAndString> {

    private static final String LOG_TAG = "IMAGE_LOADER";

    ImageView imageView;
    TextView textView;
    ProgressBar progressBar;

    int sWidth, sHeight;

    public ImageLoader(ImageView imageView, TextView textView, ProgressBar progressBar, int sHeight, int sWidth) {
        this.imageView = imageView;
        this.textView = textView;
        this.progressBar = progressBar;
        this.sHeight = sHeight;
        this.sWidth = sWidth;
    }

    @Override
    protected BitmapAndString doInBackground(String... args) {
        String baseUrl = args[0];
        String pictureId = args[1];
        try {
            URL url = new java.net.URL(baseUrl + "/img/img/" + pictureId);
            URLConnection urlConnection = url.openConnection();
            String picturId = urlConnection.getHeaderField(MyHttpClient.HEADER_FOR_PICTURE_ID);
            String score = urlConnection.getHeaderField(MyHttpClient.HEADER_FOR_PICTURE_SCORE);
            InputStream is = urlConnection.getInputStream();
            Bitmap scaledBitmap = ScaleBitmap.decodeBitmapSize(is, sHeight, sWidth);
            return new BitmapAndString(scaledBitmap, pictureId, score);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(BitmapAndString result) {
        if(result != null){
            textView.setText(result.getScore());
            imageView.setImageBitmap(result.getBitmap());
            progressBar.setVisibility(View.GONE);
        }
    }
}
