package com.example.selfie.utils.comments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.selfie.app.CommentActivity;
import com.example.selfie.utils.http.MyHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpavlov on 18.6.2014 г..
 */
public class GetComments extends AsyncTask<String, Void, List<Comment>> {

    private static String LOG_TAG = "GET_COMMENTS";
    CommentAdapter adapter;

    public GetComments(CommentAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<Comment> doInBackground(String... args) {
        List<Comment> result = new ArrayList<Comment>();
        String baseUrl = args[0];
        String selfieId = args[1];

        try{
            String jsonString = MyHttpClient.makeHttpGet(baseUrl + "/comment/get/" + selfieId);
            JSONArray jsonArray = new JSONArray(jsonString);
            for(int i = 0; i < jsonArray.length(); i++){
                result.add(convertToComment(jsonArray.getJSONObject(i)));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
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
