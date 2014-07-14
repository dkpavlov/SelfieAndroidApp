package com.example.selfie.utils.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.selfie.utils.comments.Comment;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.file.FileUtil;
import com.example.selfie.utils.http.MyHttpClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dpavlov on 10.7.2014 г..
 */
public class PostSelfie extends AsyncTask<String, Void, Boolean> {

    private SelfieDataSource dataSource;
    public static String LOG_KEY = "ADD-COMMENT";
    Context context;
    Bitmap bitmap;


    public PostSelfie(Bitmap bitmap, Context context) {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    protected Boolean doInBackground(String... args) {

        dataSource = new SelfieDataSource(context);

        String jpgAsBase64 = args[0];
        String gender = args[1];
        String type = args[2];
        String urlBase = args[3];

        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        Map<String, String> values = new HashMap<String, String>(3);
        values.put("gender", gender);
        values.put("type", type);
        values.put("base46jpg", jpgAsBase64);

        String picturId = MyHttpClient.makeHttpPostAndGetPictureId(urlBase + "/img/post", headers, values);
        if(!picturId.equals("0")){
            try{
                dataSource = new SelfieDataSource(context);
                dataSource.open();
                String fileName =  "thumb-" + picturId + ".jpg";
                Bitmap thumbBitmap = ThumbnailUtils
                        .extractThumbnail(bitmap, 100, 100);
                FileUtil.writeBitMapToFile(thumbBitmap, fileName, context);
                dataSource.createMySelfie(picturId, fileName);
                return true;
            } catch (Exception e) {
                Log.e("LOG_KEY", e.getMessage());
            } finally {
                dataSource.close();
            }
        }
        return false;
    }

    protected void onPostExecute(Boolean res) {
        if(res){
            Toast.makeText(context, "Selfie was uploaded!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "ERORR", Toast.LENGTH_LONG).show();
        }
    }
}
