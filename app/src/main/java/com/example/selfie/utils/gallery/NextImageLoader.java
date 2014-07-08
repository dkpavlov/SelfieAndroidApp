package com.example.selfie.utils.gallery;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.selfie.utils.BitmapAndString;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.http.MyHttpClient;

/**
 * Created by dkpavlov on 6/14/14.
 */
public class NextImageLoader extends AsyncTask<String, Void, BitmapAndString> {

    private static final String LOG_TAG = "NEW_IMAGE_LOADER";
    ImageView bmImage;
    StringBuffer pictureId;

    public NextImageLoader(ImageView bmImage, StringBuffer pictureId) {
        this.pictureId = pictureId;
        this.bmImage = bmImage;
    }

    protected BitmapAndString doInBackground(String... args) {
        String baseUrl = args[0];
        String pictureId = args[1];
        String gender = args[2];
        String type = args[3];
        String direction = args[4];
        Order order = Order.valueOf(args[5]);

        BitmapAndString bitmapAndString = null;

        try {
            bitmapAndString = MyHttpClient
                    .getNextImage(direction, pictureId, gender, type, order, baseUrl);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return bitmapAndString;
    }

    protected void onPostExecute(BitmapAndString result) {
        pictureId = new StringBuffer(result.getStr());
        bmImage.setImageBitmap(result.getBitmap());
    }
}
