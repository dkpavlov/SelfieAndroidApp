package com.example.selfie.utils;

import android.graphics.Bitmap;

/**
 * Created by dpavlov on 8.7.2014 г..
 */
public class BitmapAndString {

    private Bitmap bitmap;

    private String str;

    private String score;

    public BitmapAndString(Bitmap bitmap, String str, String score) {
        this.bitmap = bitmap;
        this.str = str;
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getStr() {
        return str;
    }
}
