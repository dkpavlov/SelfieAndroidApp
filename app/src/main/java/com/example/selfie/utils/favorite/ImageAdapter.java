package com.example.selfie.utils.favorite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.selfie.app.FavoriteActivity;
import com.example.selfie.app.PreviewFavoriteActivity;
import com.example.selfie.app.R;
import com.example.selfie.utils.data.Selfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.file.FileUtil;
import com.makeramen.RoundedImageView;

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
    private SelfieDataSource dataSource;
    private List<Selfie> selfieList;
    private final ImageAdapter imageAdapter = this;

    public ImageAdapter(List<Selfie> selfieList, Context context, SelfieDataSource dataSource) {
        this.selfieList = selfieList;
        this.context = context;
        this.dataSource = dataSource;
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
        final Selfie selfie = getItem(i);
        try{
            bmp = FileUtil.fileNameToBitmap(context, getItem(i).getThumbPath());
        } catch (IOException e){
            Log.e(LOG_KEY, e.getMessage());
        }
        RoundedImageView rImageView = (RoundedImageView) view.findViewById(R.id.imageView);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.delete_button);
        rImageView.setImageBitmap(bmp);
        rImageView.setClickable(true);
        rImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PreviewFavoriteActivity.class);
                i.putExtra("id", selfie.getId());
                context.startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.deleteSelfie(selfie);
                selfieList.remove(selfie);
                imageAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }
}
