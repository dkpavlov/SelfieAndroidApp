package com.example.selfie.utils.profile;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.selfie.utils.comments.Comment;
import com.example.selfie.utils.http.MyHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dpavlov on 10.7.2014 г..
 */
public class PostSelfie extends AsyncTask<String, Void, String> {

    public static String LOG_KEY = "ADD-COMMENT";
    Context context;

    public PostSelfie(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
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

        int res = MyHttpClient.makeHttpPost(urlBase + "/img/post", headers, values);
        return String.valueOf(res);
    }

    protected void onPostExecute(String res) {
        if(res.equals("200")){
            Toast.makeText(context, "Selfie was uploded!", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(context, "ERORR", Toast.LENGTH_LONG);
        }
    }
}
