package com.example.selfie.utils.data;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.selfie.app.MyUploadsActivity;
import com.example.selfie.utils.http.MyHttpClient;
import com.example.selfie.utils.upload.MySelfiesAddapter;

import java.util.List;

/**
 * Created by dkpavlov on 7/23/14.
 */
public class DeleteSelfie extends AsyncTask<String, Void, Integer> {

    private static final String ADDED_SELFIE = "DELETE_SELFIE";

    List<MySelfie> mySelfieList;
    SelfieDataSource dataSource;
    MySelfiesAddapter mySelfiesAddapter;
    MyUploadsActivity myUploadsActivity;

    private String selfieId;

    public DeleteSelfie(List<MySelfie> mySelfieList,
                        SelfieDataSource dataSource,
                        MySelfiesAddapter mySelfiesAddapter,
                        MyUploadsActivity activity) {
        this.mySelfieList = mySelfieList;
        this.dataSource = dataSource;
        this.mySelfiesAddapter = mySelfiesAddapter;
        this.myUploadsActivity = activity;
    }

    @Override
    protected Integer doInBackground(String... args) {
        try{
            String baseUrl = args[0];
            selfieId = args[1];
            int response = MyHttpClient.makeHttpGetAndGetStatusCode(baseUrl + "/img/delete/" + selfieId);
            return response;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Integer result) {
        if(result == 200){
            myUploadsActivity.removeMySelfieFromDBAndRefreshAdapter(selfieId);
        } else {
            Toast.makeText(myUploadsActivity, "Error occurred, please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}
