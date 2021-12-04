package com.example.app_em;

import androidx.appcompat.app.AppCompatActivity;
import com.example.app_em.DBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Logs";
    private View view;
    private Spinner spinner;
    private final static String TAG = "MainActivity";
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

//        setContentView(R.layout.activity_main);
    }

    public void onClickSave(View view) {
        dbHelper = new DBHelper(this);

        ContentValues cv = new ContentValues();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        this.view = view;

        spinner = (Spinner) findViewById(R.id.spinner);
        String selectedView = String.valueOf(spinner.getSelectedItem());

        // отправить результат selectedView (мб в int) в бд
        Intent intent = new Intent(this, StatisticActivity.class);

        Date date = Calendar.getInstance().getTime();
        //Дата и время
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        String strDate = dateFormat.format(date);
        String strTime = timeFormat.format(date);

        Log.d(LOG_TAG, "--- Insert in Database Emotions: ---");

        cv.put("date", strDate);
        cv.put("time", strTime);
        cv.put("emotion", String.valueOf(selectedView));

        long rowID = db.insert("EmotionDB", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        dbHelper.close();
        startActivity(intent);
    }

    public void onClickSkip(View view) {
        this.view = view;
        Intent intent = new Intent(this, StatisticActivity.class);
        startActivity(intent);
    }

    public void onClickDelete(View view) {
        this.view = view;
        dbHelper = new DBHelper(this);

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Clear EmotionDB: ---");
        // удаляем все записи
        int clearCount = db.delete("EmotionDB", null, null);
        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
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