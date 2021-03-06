package com.example.selfie.utils.upload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.example.selfie.app.GalleryActivity;
import com.example.selfie.app.HomeActivity;
import com.example.selfie.app.MyUploadsActivity;
import com.example.selfie.app.PreviewFavoriteActivity;
import com.example.selfie.app.PreviewMyUploadActivity;
import com.example.selfie.app.R;
import com.example.selfie.utils.data.DeleteSelfie;
import com.example.selfie.utils.data.MySelfie;
import com.example.selfie.utils.data.Selfie;
import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.favorite.ImageAdapter;
import com.example.selfie.utils.file.FileUtil;
import com.makeramen.RoundedImageView;

import java.io.IOException;
import java.util.List;

/**
 * Created by dkpavlov on 7/13/14.
 */
public class MySelfiesAddapter extends BaseAdapter{

    private static final String LOG_KEY = "IMAGE_ADAPTER";
    private MyUploadsActivity activity;
    private SelfieDataSource dataSource;
    private List<MySelfie> mySelfiesList;
    private final MySelfiesAddapter mySelfiesAddapter = this;

    public MySelfiesAddapter(List<MySelfie> selfieList, MyUploadsActivity activity, SelfieDataSource dataSource) {
        this.mySelfiesList = selfieList;
        this.activity = activity;
        this.dataSource = dataSource;
    }

    public void removeItem(String selfieId){
        removeSelfieBySelfieId(selfieId);
        mySelfiesAddapter.notifyDataSetChanged();
    }

    private void removeSelfieBySelfieId(String selfieId){
        for(MySelfie s: mySelfiesList){
            if(s.getSelfieId().equals(selfieId)){
                mySelfiesList.remove(s);
                break;
            }
        }
    }

    @Override
    public int getCount() {
        return mySelfiesList.size();
    }

    @Override
    public MySelfie getItem(int i) {
        return mySelfiesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(mySelfiesList != null){
            return mySelfiesList.get(i).getId();
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.grid_cell, viewGroup, false);
        }
        Bitmap bmp = null;
        final MySelfie selfie = getItem(i);
        try{
            bmp = FileUtil.fileNameToBitmap(activity, getItem(i).getThumbName());
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
                Intent i = new Intent(activity, PreviewMyUploadActivity.class);
                i.putExtra("id", selfie.getSelfieId());
                activity.startActivity(i);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteSelfie(mySelfiesList, dataSource, mySelfiesAddapter, activity)
                        .execute(GalleryActivity.WEB_SERVICE, String.valueOf(selfie.getSelfieId()));
            }
        });
        return view;
    }
}
