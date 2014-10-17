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
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
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
    public static final String HEADER_FOR_PICTURE_ID = "Picture-id";
    public static final String HEADER_FOR_PICTURE_SCORE = "Picture-score";
    public static final String HEADER_FOR_PICTURE_COMMENT_COUNT = "Picture-comments";
    public static final String HEADER_FOR_PICTURE_FAVORITE_COUNT = "Picture-favorite";

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

    public static int makeHttpGetAndGetStatusCode(String url){
        HttpResponse httpResponse;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try{
            return httpClient.execute(httpGet).getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;

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

    public static String makeHttpPostAndGetPictureId(String url,
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
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                return httpResponse.getHeaders(HEADER_FOR_PICTURE_ID)[0].getValue();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());

        }
        return "0";
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
        String newPictureId, score, commentCount, favoriteCount;
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
        InputStream is = urlConnection.getInputStream();
        newPictureId = urlConnection.getHeaderField(HEADER_FOR_PICTURE_ID);
        score = urlConnection.getHeaderField(HEADER_FOR_PICTURE_SCORE);
        commentCount = urlConnection.getHeaderField(HEADER_FOR_PICTURE_COMMENT_COUNT);
        favoriteCount = urlConnection.getHeaderField(HEADER_FOR_PICTURE_FAVORITE_COUNT);
        return new BitmapAndString(ScaleBitmap.decodeBitmapSize(is, sHeight, sWidth),
                newPictureId, score, commentCount, favoriteCount);
    }

    public static BitmapAndString getNewestPicture(String gender,
                                                   String type,
                                                   String baseURL,
                                                   int sHeight,
                                                   int sWidth) throws IOException {
        String newPictureId, score, commentCount, favoriteCount;
        URL url = new URL(baseURL + "/img/new/order/"
                            + gender + "/"
                            + type);
        URLConnection urlConnection = url.openConnection();
        newPictureId = urlConnection.getHeaderField(HEADER_FOR_PICTURE_ID);
        score = urlConnection.getHeaderField(HEADER_FOR_PICTURE_SCORE);
        commentCount = urlConnection.getHeaderField(HEADER_FOR_PICTURE_COMMENT_COUNT);
        favoriteCount = urlConnection.getHeaderField(HEADER_FOR_PICTURE_FAVORITE_COUNT);
        return new BitmapAndString(ScaleBitmap.decodeStream(urlConnection.getInputStream(), sHeight, sWidth), newPictureId, score, commentCount, favoriteCount);
    }

    public static boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
