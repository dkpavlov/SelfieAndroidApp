package com.example.selfie.utils.favorite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.selfie.app.R;
import com.example.selfie.utils.data.Selfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.file.FileUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by dkpavlov on 6/25/14.
 */
public class ImageAdapter extends BaseAdapter {

    private static final String LOG_KEY = "IMAGE_ADAPTER";
    private Context context;
    private List<Selfie> selfieList;

    public ImageAdapter(List<Selfie> selfieList, Context context) {
        this.selfieList = selfieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return selfieList.size();
    }

    @Override
    public Selfie getItem(int i) {
        return selfieList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(selfieList != null){
            return selfieList.get(i).getId();
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_cell, viewGroup, false);
        }
        Bitmap bmp = null;
        try{
            bmp = FileUtil.fileNameToBitmap(context, getItem(i).getThumbPath());
        } catch (IOException e){
            Log.e(LOG_KEY, e.getMessage());
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageBitmap(bmp);
        return view;
    }
}
