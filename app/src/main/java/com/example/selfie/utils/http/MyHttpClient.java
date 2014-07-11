package com.example.selfie.utils.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.selfie.utils.BitmapAndString;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.ui.ScaleBitmap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dpavlov on 24.6.2014 г..
 */
public class MyHttpClient  {

    public static final String LOG_TAG = "MY_HTTP_CLIENT";
    private static final String HEADER_FOR_PICTURE_ID = "Picture-id";
    private static final String HEADER_FOR_PICTURE_SCORE = "Picture-score";

    public static String makeHttpGet(String url){
        HttpResponse httpResponse;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try{
            httpResponse = httpClient.execute(httpGet);
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    public static String makeHttpGetWithReturnJSON(String url,
                                                   Map<String, String> headers,
                                                   Map<String, String> values){
        HttpResponse httpResponse;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(values.size());

        for(String key: headers.keySet()){
            httpPost.setHeader(key, headers.get(key));
        }
        for(String key: values.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key, values.get(key)));
        }
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    public static int makeHttpPost(String url,
                                      Map<String, String> headers,
                                      Map<String, String> values){

        HttpResponse httpResponse;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(headers != null){
            for(String key: headers.keySet()){
                httpPost.setHeader(key, headers.get(key));
            }
        }
        if(values != null){
            for(String key: values.keySet()){
                nameValuePairs.add(new BasicNameValuePair(key, values.get(key)));
            }
        }
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return 0;
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static BitmapAndString getNextImage(String direction,
                                               String currentPictureId,
                                               String gender,
                                               String type,
                                               Order order,
                                               String baseURL,
                                               int sHeight,
                                               int sWidth) throws IOException {

        URL url = null;
        Bitmap bitmap;
        String newPictureId, score;
        switch (order){
            case ORDERED:
                url = new URL(baseURL + "/img/order"
                                + "/" + gender
                                + "/" + type
                                + "/" + direction
                                + "/" + currentPictureId);
                break;

            case RANDOMIZED:
                url = new URL(baseURL + "/img/random"
                        + "/" + gender
                        + "/" + type
                        + "/" + currentPictureId);
                break;
        }


        URLConnection urlConnection = url.openConnection();
        InputStream in = urlConnection.getInputStream();
        newPictureId = urlConnection.getHeaderField(HEADER_FOR_PICTURE_ID);
        score = urlConnection.getHeaderField(HEADER_FOR_PICTURE_SCORE);
        bitmap = ScaleBitmap.scale(sWidth, sHeight, BitmapFactory.decodeStream(in));
        return new BitmapAndString(bitmap, newPictureId, score);
    }

    public static BitmapAndString getNewestPicture(String gender,
                                                   String type,
                                                   String baseURL,
                                                   int sHeight,
                                                   int sWidth) throws IOException {
        URL url = new URL(baseURL + "/img/new/order/"
                            + gender + "/"
                            + type);
        URLConnection urlConnection = url.openConnection();
        String newPictureId = urlConnection.getHeaderField(HEADER_FOR_PICTURE_ID);
        String score = urlConnection.getHeaderField(HEADER_FOR_PICTURE_SCORE);
        Bitmap bitmap = ScaleBitmap.scale(sWidth, sHeight, BitmapFactory.decodeStream(urlConnection.getInputStream()));
        return new BitmapAndString(bitmap, newPictureId, score);
    }
}
