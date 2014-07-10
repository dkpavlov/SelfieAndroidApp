package com.example.selfie.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.selfie.utils.data.Selfie;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dpavlov on 26.6.2014 г..
 */
public class FileUtil {
    public static void writeBitMapToFile(Bitmap bitmap, String filename, Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        fos.write(outputStream.toByteArray());
        fos.flush();
        fos.close();
    }

    public static Bitmap fileNameToBitmap(Context context, String filename) throws FileNotFoundException {
        FileInputStream inputStream = context.openFileInput(filename);
        BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
        return BitmapFactory.decodeStream(bufferedInput);
    }

    public static void deleteSelfieFiles(Selfie selfie, Context context){
        context.deleteFile(selfie.getPath());
        context.deleteFile(selfie.getThumbPath());
    }

    public static final String uriToBase64(Uri uri, Context context) throws IOException{
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        byte[] inputData = getBytes(iStream);
        return Base64.encodeToString(inputData, Base64.DEFAULT);
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
