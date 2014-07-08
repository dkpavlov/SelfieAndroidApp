package com.example.selfie.utils.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.selfie.utils.file.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dpavlov on 18.6.2014 г..
 */
public class AddSelfieToFavorites extends AsyncTask<String, Void, String> {

    private static final String ADDED_SELFIE = "PICTUR_IS_ALREADY_ADDED";

    ImageView imageView;
    SelfieDataSource dataSource;
    Context context;

    public AddSelfieToFavorites(ImageView imageView,
                                SelfieDataSource dataSource,
                                Context context) {
        this.imageView = imageView;
        this.dataSource = dataSource;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        String selfieId = args[0];
        String fileName = selfieId + ".jpg";

        if(!dataSource.findByPath(fileName)){
            return ADDED_SELFIE;
        }

        try{
            Bitmap bm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            FileUtil.writeBitMapToFile(bm, fileName, context);

            Bitmap thumbBitmap = ThumbnailUtils.extractThumbnail(bm, 100, 100);
            FileUtil.writeBitMapToFile(thumbBitmap, "thumb-"+fileName, context);
            dataSource.createSelfie(fileName, "thumb-"+fileName);
        }catch (Exception e){
            Log.e("File Downloade: ", e.getMessage());
        }
        return fileName;
    }

    protected void onPostExecute(String result) {
        if(result.equals(ADDED_SELFIE)){
            Toast.makeText(context, "Picture is already added to favorites", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Picture was added to favorites", Toast.LENGTH_LONG).show();
        }
    }



}
