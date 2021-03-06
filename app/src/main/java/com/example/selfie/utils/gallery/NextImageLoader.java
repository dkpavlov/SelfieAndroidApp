package com.example.selfie.utils.gallery;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.selfie.utils.BitmapAndString;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.Utils;
import com.example.selfie.utils.http.MyHttpClient;

/**
 * Created by dkpavlov on 6/14/14.
 */
public class NextImageLoader extends AsyncTask<String, Void, BitmapAndString> {

    private static final String LOG_TAG = "NEW_IMAGE_LOADER";
    ImageView bmImage;
    StringBuilder pictureId;
    TextView scoreTextView, commentCountView, favoriteCountView;
    ProgressBar progressBar;
    int sHeight, sWidth;
    Context context;

    public NextImageLoader(ImageView bmImage, StringBuilder pictureId,
                           TextView scoreTextView, TextView commentCountView, TextView favoriteCountView,
                           ProgressBar progressBar, int sHeight, int sWidth, Context context) {
        this.pictureId = pictureId;
        this.bmImage = bmImage;
        this.scoreTextView = scoreTextView;
        this.commentCountView = commentCountView;
        this.favoriteCountView = favoriteCountView;
        this.progressBar = progressBar;
        this.sHeight = sHeight;
        this.sWidth = sWidth;
        this.context = context;
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
                    .getNextImage(direction, pictureId, gender, type, order, baseUrl, sHeight, sWidth);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return bitmapAndString;
    }

    protected void onPostExecute(BitmapAndString result) {
        if(result != null){
            pictureId.replace(0, pictureId.length(), result.getStr());
            bmImage.setImageBitmap(result.getBitmap());
            progressBar.setVisibility(View.GONE);
            scoreTextView.setText(result.getScore());
            commentCountView.setText(result.getCommentCount());
            favoriteCountView.setText(result.getFavoriteCount());
        } else {
            Utils.startHomeActivityWithToast(context);
        }
    }
}
