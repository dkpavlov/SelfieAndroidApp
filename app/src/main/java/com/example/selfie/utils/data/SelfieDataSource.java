package com.example.selfie.utils.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpavlov on 18.6.2014 г..
 */
public class SelfieDataSource {

    private SQLiteDatabase database;
    private SelfieSQLHelper sqlHelper;
    private String[] allColumns = {SelfieSQLHelper.COLUMN_ID,
                                   SelfieSQLHelper.COLUMN_SELFIE_PATH,
                                   SelfieSQLHelper.COLUMN_SELFIE_THUMB_PATH};

    public SelfieDataSource(Context context){
        sqlHelper = new SelfieSQLHelper(context);
    }

    public void open() {
        database = sqlHelper.getWritableDatabase();
    }

    public void close(){
        sqlHelper.close();
    }

    public Selfie createSelfie(String path, String thumbPath){
        ContentValues values = new ContentValues();
        values.put(SelfieSQLHelper.COLUMN_SELFIE_PATH, path);
        values.put(SelfieSQLHelper.COLUMN_SELFIE_THUMB_PATH, thumbPath);
        long insertId = database.insert(SelfieSQLHelper.TABLE_FAVORITE_SELFIES, null, values);
        Cursor cursor = database.query(SelfieSQLHelper.TABLE_FAVORITE_SELFIES, allColumns,
                SelfieSQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Selfie newSelfie = cursorToSelfie(cursor);
        cursor.close();
        return newSelfie;
    }

    public void createMySelfie(String selfieId, String thumb){
        ContentValues values = new ContentValues();
        values.put(SelfieSQLHelper.COLUMN_MY_SELFIE_ID, selfieId);
        values.put(SelfieSQLHelper.COLUMN_SELFIE_THUMB_PATH, thumb);
        database.insert(SelfieSQLHelper.TABLE_MY_SELFIES, null, values);
    }

    public Boolean findByPath(String path){
        Cursor cursor = database.query(SelfieSQLHelper.TABLE_FAVORITE_SELFIES, allColumns,
                SelfieSQLHelper.COLUMN_SELFIE_PATH + " = '" + path + "'", null, null, null, null);
        cursor.moveToFirst();
        return cursor.isAfterLast();
    }

    public Selfie findById(long id){
        Cursor cursor = database.query(SelfieSQLHelper.TABLE_FAVORITE_SELFIES, allColumns,
                SelfieSQLHelper.COLUMN_ID + " = '" + id + "'", null, null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            return cursorToSelfie(cursor);
        }
        return null;
    }

    public void deleteSelfie(Selfie selfie){
        long id = selfie.getId();
        database.delete(SelfieSQLHelper.TABLE_FAVORITE_SELFIES, SelfieSQLHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Selfie> getAllSelfis(){
        List<Selfie> selfieList = new ArrayList<Selfie>();
        Cursor cursor = database
                .rawQuery("select * from "+SelfieSQLHelper.TABLE_FAVORITE_SELFIES, null);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                selfieList.add(cursorToSelfie(cursor));
                cursor.moveToNext();
            }
        }
        return selfieList;

    }

    public List<MySelfie> getAllMySelfies(){
        List<MySelfie> mySelfiesList = new ArrayList<MySelfie>();
        Cursor cursor = database
                .rawQuery("select * from " + SelfieSQLHelper.TABLE_MY_SELFIES, null);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                mySelfiesList.add(cursorToMySelfie(cursor));
                cursor.moveToNext();
            }
        }
        return mySelfiesList;
    }

    private Selfie cursorToSelfie(Cursor cursor) {
        Selfie selfie = new Selfie();
        selfie.setId(cursor.getLong(0));
        selfie.setPath(cursor.getString(1));
        selfie.setThumbPath(cursor.getString(2));
        return selfie;
    }

    private MySelfie cursorToMySelfie(Cursor cursor) {
        MySelfie mySelfie = new MySelfie();
        mySelfie.setId(cursor.getLong(0));
        mySelfie.setSelfieId(cursor.getString(1));
        mySelfie.setThumbName(cursor.getString(2));
        return mySelfie;
    }



}
