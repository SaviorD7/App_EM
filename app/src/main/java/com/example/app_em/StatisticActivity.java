package com.example.app_em;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StatisticActivity extends AppCompatActivity {

    private final static String TAG = "StatisticActivity";
    private static final String LOG_TAG = "Logs";
    private View view;
    Float i = Float.valueOf(0);

    DBHelper dbHelper;

    Spinner spinnerMood;
    EditText timeStart;
    EditText timeStop;

    HashMap<Float, String> dateMap = new HashMap<Float, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetChart();
    }

    public void SetChart()
    {
        LineChart chart = (LineChart) findViewById(R.id.any_chart_view);

        IAxisValueFormatter formatter = new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String result = dateMap.get(value);
                if (result == null)
                {
                    return "-";
                }
                else
                {
                    return result;
                }
            }
        };

        LineData data = new LineData(getData());
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(60f);
        xAxis.setValueFormatter(formatter);

        chart.setData(data);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    public ArrayList getData() {
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

                if (c.getString(emotion_db) != null && !c.getString(emotion_db).isEmpty()) {
                    Integer emotionInt = c.getInt(emotion_db);

                    // TODO: check emoInt
                        dateMap.put(i, c.getString(date_db));
                        Entry entry = new Entry(i, emotionInt);
                        i++;
                        current_data.add(entry);
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
        ArrayList dataSets = new ArrayList();


        if (!current_data.isEmpty()){
            LineDataSet dataSet = new LineDataSet(current_data, "Emotions");
        dataSet.setColor(Color.rgb(0, 155, 0));
        dataSet.disableDashedLine();
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawIcons(false);
        dataSet.setForm(Legend.LegendForm.EMPTY);
        dataSet.setLabel("");

        dataSets.add(dataSet);
    }

        return dataSets;
    }

    public void onClickCalculate(View view)
    {
        this.view = view;

        spinnerMood = (Spinner) findViewById(R.id.spinner2);
        String selectedMood = String.valueOf(spinnerMood.getSelectedItem());

        timeStart = (EditText) findViewById(R.id.editTextTime);
        String selectedStartString = timeStart.getText().toString();

        timeStop = (EditText) findViewById(R.id.editTextTime2);
        String selectedStopString = timeStop.getText().toString();

        try {
            DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            Date selectedStart = formatter.parse(selectedStartString);
            Date selectedStop = formatter.parse(selectedStopString);
            Log.d(TAG, "START TIME: " + selectedStartString);
            Log.d(TAG, "STOP TIME: " + selectedStopString);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        //TODO:
        // Вернуть данные в виде ?
        // проверка вводимых данных ?
        // вычислить верочтность

        onClickQuery(selectedStartString, selectedStopString, getEmotionsCode(selectedMood));

        createOneButtonAlertDialog("", selectedStartString, selectedStopString, selectedMood);
    }

    private void createOneButtonAlertDialog(String probability, String startTime, String stopTime, String mood) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticActivity.this);

        builder.setTitle("Статистика настроения");
        builder.setMessage("бла бла бла");

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // showMessage("Нажали ОК");
                    }
                });

        builder.show();
    }

    private void showMessage(String textInMessage) {
        Toast.makeText(getApplicationContext(), textInMessage, Toast.LENGTH_LONG).show();
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

        SetChart();
    }

    @SuppressLint("Range")
    public Long onClickQuery(String startTime, String stopTime, Integer EmotionID) {
        dbHelper = new DBHelper(this);
        
//        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//        Date DBdate = new Date();
//
//        try {
//            DBdate = timeFormat.parse(startTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        Cursor c = null;


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Connected EmotionDB: ---");

        String selection;
        Long count;

        count = DatabaseUtils.queryNumEntries(db, "EmotionDB",
                "emotion =? AND time between ? AND ?", new String[]{String.valueOf(EmotionID),startTime,stopTime});

        Log.d(LOG_TAG, "NUMBER OF DATA IS: " + count);

        dbHelper.close();

        SetChart();
        return count;
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