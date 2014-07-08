package com.example.selfie.utils.favorite;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.selfie.utils.file.FileUtil;

import java.io.FileNotFoundException;

/**
 * Created by dpavlov on 26.6.2014 г..
 */
public class UpdateImageViewInAsync extends AsyncTask<String, Void, Bitmap> {

    private static final String LOG_KEY = "ASYNC_IMAGE_UPDATE";

    private ImageView imageView;
    private Context context;

    public UpdateImageViewInAsync(ImageView imageView, Context context) {
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... args) {
        String filename = args[0];
        Bitmap bitmap = null;
        try{
            bitmap = FileUtil.fileNameToBitmap(context, filename);
        } catch (FileNotFoundException e) {
            Log.e(LOG_KEY, e.getMessage());
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
