package com.example.app_em;


import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "Logs";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "EmotionDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table EmotionDB ("
                + "id integer primary key autoincrement,"
                + "date text,"
                + "time text,"
                + "emotion text" + ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}