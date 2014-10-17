package com.example.selfie.utils.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    public static Bitmap decodeBitmapSize(InputStream is, int height, int width) throws IOException{
        byte[] bytes = iSToBytes(is);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;
        Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return b;
    }

    @Deprecated
    public static Bitmap decodeBitmapSizeBufferedInputStream(BufferedInputStream bis, int height, int width) throws IOException{
        bis.mark(2 * 1024 * 1024);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bis, null, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);

        bis.reset();
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(bis, null, options);
    }

    public static Bitmap decodeUriToScaledBitmap(Context context, Uri uri, int height, int width) throws IOException{
        InputStream is = context.getContentResolver().openInputStream(uri);
        return decodeBitmapSize(is, height, width);
    }

    public static Bitmap decodeStream(InputStream in, int height, int width){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 3];
            int len;
            while ((len = in.read(buffer)) > -1 ) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
            InputStream is2 = new ByteArrayInputStream(baos.toByteArray());

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is1,null,o);

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=calculateInSampleSize(o, width, height);

            return BitmapFactory.decodeStream(is2, null, o2);
        } catch (Exception e){
            return null;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth,
                                             int reqHeight) {
        int inSampleSize = 1;
        final int maxSize = reqHeight * reqWidth;
        while ((options.outWidth * options.outHeight) * (1 / Math.pow(inSampleSize, 2)) >
                maxSize) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }


    private static byte[] iSToBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024 * 1024]; //1MB

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }
}
