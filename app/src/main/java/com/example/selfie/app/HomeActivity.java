package com.example.selfie.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfie.utils.Order;
import com.example.selfie.utils.http.MyHttpClient;

public class HomeActivity extends Activity {

    public static final String PICTURES_TYPE_KEY = "PICTURES_TYPE";
    public static final String TYPE_OF_CONTENT_KEY = "TYPE_OF_CONTENT";
    public static final String ORDER_TYPE_KEY = "ORDER_TYPE_KEY";

    public static final String NSFW = "NSFW";
    public static final String SFW = "SFW";

    public static final String NEWEST = "NEWEST";

    TextView textView;
    Button girlsButton;
    Button boysButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onButtonClick(View v){

        if(!MyHttpClient.isNetworkAvailable(this)){
            Toast toast = Toast.makeText(getApplicationContext(), "Network connection is required", Toast.LENGTH_LONG);
            toast.show();
        } else {
            String picturesType = null;
            switch (v.getId()){
                case R.id.girls_button:
                    picturesType = "FEMALE";
                    break;
                case R.id.boys_button:
                    picturesType = "MALE";
                    break;
            }

            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra(TYPE_OF_CONTENT_KEY, SFW);
            intent.putExtra(ORDER_TYPE_KEY, Order.RANDOMIZED.toString());
            intent.putExtra(PICTURES_TYPE_KEY, picturesType);
            startActivity(intent);
        }
    }
}
