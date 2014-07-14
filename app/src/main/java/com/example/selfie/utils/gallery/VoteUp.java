package com.example.selfie.utils.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfie.utils.BitmapAndString;
import com.example.selfie.utils.MyAccountManager;
import com.example.selfie.utils.http.MyHttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dpavlov on 11.7.2014 г..
 */
public class VoteUp extends AsyncTask<String, Void, Integer> {

    Context context;
    TextView textView;

    public VoteUp(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    @Override
    protected Integer doInBackground(String... args) {
        String baseURL = args[0];
        String pictureId = args[1];
        Map<String, String> values = new HashMap<String, String>();
        values.put("id", pictureId.toString());
        values.put("email", MyAccountManager.getFirstGoogleAccUsername(context));
        int responseCode = MyHttpClient.makeHttpPost(baseURL + "/voteUp/vote", null, values);
        return Integer.valueOf(responseCode);
    }

    protected void onPostExecute(Integer result) {
        if(result.equals(200)){
            Integer score = Integer.valueOf((String) textView.getText());
            Integer newScore = score + 1;
            textView.setText(newScore.toString());
            Toast.makeText(context, "Thank you for voting", Toast.LENGTH_LONG).show();
        } else if (result.equals(403)) {
            Toast.makeText(context, "You already voted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show();
        }
    }
}
