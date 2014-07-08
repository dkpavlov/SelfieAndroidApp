package com.example.selfie.utils.comments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.selfie.utils.MyAccountManager;
import com.example.selfie.utils.http.MyHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dkpavlov on 6/17/14.
 */
public class AddComment extends AsyncTask<String, Void, List<Comment>> {

    public static String LOG_KEY = "ADD-COMMENT";

    CommentAdapter adapter;
    Context context;

    public AddComment(CommentAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected List<Comment> doInBackground(String... args) {
        List<Comment> result = new ArrayList<Comment>();
        String baseUrl = args[0];
        String comment = args[1];
        String user = args[2];
        String selfieId = args[3];

        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        Map<String, String> values = new HashMap<String, String>(2);
        values.put("comment", comment);
        values.put("user", MyAccountManager.getFirstGoogleAccUsername(context));

        try{
            String jsonString = MyHttpClient.makeHttpGetWithReturnJSON(baseUrl + "/comment/add/" + selfieId,
                                                                        headers,
                                                                        values);
            JSONArray jsonArray = new JSONArray(jsonString);
            for(int i = 0; i < jsonArray.length(); i++){
                result.add(convertToComment(jsonArray.getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e(LOG_KEY, e.getMessage());
        }
        return result;
    }

    protected void onPostExecute(List<Comment> result) {
        adapter.setCommentList(result);
        adapter.notifyDataSetChanged();
    }

    private Comment convertToComment(JSONObject obj) throws JSONException {
        String text = obj.getString("text");
        String user = obj.getString("user");
        return new Comment(text, user);
    }
}
