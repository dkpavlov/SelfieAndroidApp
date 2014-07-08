package com.example.selfie.utils.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.selfie.utils.data.Selfie;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
