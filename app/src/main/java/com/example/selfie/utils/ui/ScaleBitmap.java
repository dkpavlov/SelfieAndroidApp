package com.example.selfie.utils.ui;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by dpavlov on 11.7.2014 г..
 */
public class ScaleBitmap {
    public static Bitmap scale(int width, int height, Bitmap b){
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse), (int) (b.getHeight() * factorToUse), false);
    }
}
