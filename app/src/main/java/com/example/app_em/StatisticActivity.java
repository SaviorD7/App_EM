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

        // ЭТО ТУТ ВРЕМЕННО НЕ ЗНАЮ КУДА ДЕТЬ
        List<List<String>> data = getDataFromDB();

        Intent intent = new Intent(this, MainActivity.class);
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

    public List<List<String>> getDataFromDB(){
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<List<String>> data = new ArrayList<List<String>>();
        List<String> current_data = new ArrayList<>();


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
                current_data.add(c.getString(ID_db));
                current_data.add(c.getString(date_db));
                current_data.add(c.getString(time_db));
                current_data.add(c.getString(emotion_db));

                data.add(current_data);
                current_data = new ArrayList<>();


                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(ID_db) +
                                ", date = " + c.getString(date_db) +
                                ", time = " + c.getString(time_db) +
                                ", emotion = " + c.getString(emotion_db));
            } while (c.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
            System.out.println(java.util.Arrays.toString(data.toArray()));
        }
        c.close();
        dbHelper.close();

        return  data;

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