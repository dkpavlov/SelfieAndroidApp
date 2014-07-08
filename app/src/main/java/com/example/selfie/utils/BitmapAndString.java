package com.example.selfie.utils;

import android.graphics.Bitmap;

/**
 * Created by dpavlov on 8.7.2014 г..
 */
public class BitmapAndString {

    private Bitmap bitmap;

    private String str;

    public BitmapAndString(Bitmap bitmap, String str) {
        this.bitmap = bitmap;
        this.str = str;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getStr() {
        return str;
    }
}
