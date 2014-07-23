package com.example.selfie.utils;

import android.graphics.Bitmap;

/**
 * Created by dpavlov on 8.7.2014 г..
 */
public class BitmapAndString {

    private Bitmap bitmap;

    private String str;

    private String score;

    private String commentCount;

    private String favoriteCount;

    public BitmapAndString(Bitmap bitmap, String str, String score, String commentCount, String favoriteCount) {
        this.bitmap = bitmap;
        this.str = str;
        this.score = score;
        this.commentCount = commentCount;
        this.favoriteCount = favoriteCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getFavoriteCount() {
        return favoriteCount;
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
