package com.example.app_em;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    private final static String TAG = "StatisticActivity";
    private static final String LOG_TAG = "Logs";
    private View view;
    Float i = Float.valueOf(0);

    DBHelper dbHelper;

    HashMap<Float, String> dateMap = new HashMap<Float, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart chart = (LineChart) findViewById(R.id.any_chart_view);

        IAxisValueFormatter formatter = new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dateMap.get(value);
            }
        };

        LineData data = new LineData(getData());
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(90f);
        xAxis.setValueFormatter(formatter);

        chart.setData(data);
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    public ArrayList getData(){
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList current_data = new ArrayList();

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
                // TODO: заменить i на Date
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//                Date DBdate = new Date();
//
//                try {
//                    DBdate = dateFormat.parse(c.getString(date_db));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                if (c.getString(emotion_db) != null && !c.getString(emotion_db).isEmpty())
                {
                    String emotionString = c.getString(emotion_db);
                    Integer emotionInt = getEmotionsCode(emotionString);
                    if (emotionInt != -1) {
                        dateMap.put(i, c.getString(date_db));
                        Entry entry = new Entry(i, emotionInt);
                        i++;
                        current_data.add(entry);
                    }
                }

                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(ID_db) +
                                ", date = " + c.getString(date_db) +
                                ", time = " + c.getString(time_db) +
                                ", emotion = " + c.getString(emotion_db));
            } while (c.moveToNext());
        } else {
            Log.d(LOG_TAG, "0 rows");
            System.out.println(java.util.Arrays.toString(current_data.toArray()));
        }
        c.close();
        dbHelper.close();

        LineDataSet dataSet = new LineDataSet(current_data, "Emotions");
        dataSet.setColor(Color.rgb(0, 155, 0));
        dataSet.disableDashedLine();

        ArrayList dataSets = new ArrayList();
        dataSets.add(dataSet);

        return dataSets;
    }

    public void onClickSurvey(View view)
    {
        this.view = view;

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

    public Integer getEmotionsCode(String str) {
        if (str.equals(Emotions.BEST.decode))
        {
            Log.d(LOG_TAG, "1");
            return Emotions.BEST.label;
        }
        else if (str.equals(Emotions.PERFECT.decode))
        {
            return Emotions.PERFECT.label;
        }
        else if (str.equals(Emotions.GOOD.decode))
        {
            return Emotions.GOOD.label;
        }
        else if (str.equals(Emotions.NORMAL.decode))
        {
            return Emotions.NORMAL.label;
        }
        else if (str.equals(Emotions.SOSO.decode))
        {
            return Emotions.SOSO.label;
        }
        else if (str.equals(Emotions.BAD.decode))
        {
            return Emotions.BAD.label;
        }
        else
        {
            return -1;
        }
    }
}