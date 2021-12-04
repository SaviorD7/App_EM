package com.example.app_em;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    private final static String TAG = "StatisticActivity";
    private static final String LOG_TAG = "Logs";
    private View view;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSurvey(View view)
    {
        this.view = view;
//        Intent intent = new Intent(this, StatisticActivity.class);
        getDataFromDB();
//        startActivity(intent);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        setContentView(R.layout.activity_survey);

    }

    public void getDataFromDB(){
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<ArrayList<String>> data = new ArrayList<>();
        List<ArrayList<String>> current_data = new ArrayList<>();


        ArrayList<String> ID;
        ArrayList<String> date;
        ArrayList<String> time;
        ArrayList<String> emotion;



        Log.d(LOG_TAG, "--- Rows in EmotionDB: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("EmotionDB", null, null, null, null, null, null);


        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int ID_db = c.getColumnIndex("id");
            int date_db = c.getColumnIndex("date");
            int time_db = c.getColumnIndex("time");
            int emotion_db = c.getColumnIndex("emotion");

            do {
                ID = ID = new ArrayList<String>();
                date = new ArrayList<String>();
                time = new ArrayList<String>();
                emotion = new ArrayList<String>();

                ID.add(c.getString(ID_db));
                date.add(c.getString(date_db));
                time.add(c.getString(time_db));
                emotion.add(c.getString(emotion_db));
                data.add(ID);
                data.add(date);
                data.add(time);
                data.add(emotion);

                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(ID_db) +
                                ", date = " + c.getString(date_db) +
                                ", time = " + c.getString(time_db) +
                                ", emotion = " + c.getString(emotion_db));
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
            System.out.println(java.util.Arrays.toString(data.toArray()));
        c.close();
        dbHelper.close();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart");
    }


}