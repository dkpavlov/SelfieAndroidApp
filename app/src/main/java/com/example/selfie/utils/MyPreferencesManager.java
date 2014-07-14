package com.example.selfie.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dpavlov on 14.7.2014 г..
 */
public class MyPreferencesManager {

    public static final String PREFS_NAME = "SELFIE_PREFS";
    public static final String SELFIE_TYPE = "SELFIE_TYPE";
    public static final String SELFIE_GENDER = "SELFIE_GENDER";
    public static final String SELFIE_ORDER = "SELFIE_ORDER";

    Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public MyPreferencesManager(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
    }

    public String getPreferences(String preferenceKay, String preferenceDefVal){
        return settings.getString(preferenceKay, preferenceDefVal);
    }

    public void setPreferences(String preferenceKay, String preferenceVal){
        editor.putString(preferenceKay, preferenceVal);
        editor.commit();
    }


}
