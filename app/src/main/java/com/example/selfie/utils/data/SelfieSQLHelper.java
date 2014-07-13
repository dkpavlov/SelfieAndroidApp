package com.example.selfie.utils.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dpavlov on 18.6.2014 г..
 */
public class SelfieSQLHelper extends SQLiteOpenHelper {

    public static final String TABLE_FAVORITE_SELFIES = "favorite_selfies";
    public static final String TABLE_MY_SELFIES = "my_selfies";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SELFIE_PATH = "path";
    public static final String COLUMN_SELFIE_THUMB_PATH = "thumb_path";

    public static final String COLUMN_MY_SELFIE_ID = "selfie_id";

    private static final String DATABASE_NAME = "selfie.db";
    private static final int DATABASE_VERSION = 4;

    private static final String CREATE_TABLE_FAVORITE_SELFIES = "create table "
            + TABLE_FAVORITE_SELFIES + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_SELFIE_PATH + " text not null, " +
            COLUMN_SELFIE_THUMB_PATH + " text not null);";

    private static final String CREATE_TABLE_MY_SELFIES = "create table "
            + TABLE_MY_SELFIES + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MY_SELFIE_ID + " text not null, "
            + COLUMN_SELFIE_THUMB_PATH + " text not null);";

    public SelfieSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_FAVORITE_SELFIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_MY_SELFIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_SELFIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_SELFIES);
        onCreate(sqLiteDatabase);
    }
}
