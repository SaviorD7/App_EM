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
import com.github.mikephil.charting.components.YAxis;
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
    HashMap<Float, String> emoMap = new HashMap<Float, String>();


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

        IAxisValueFormatter formatterEmo = new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String result = emoMap.get(value);
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

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1f);
//        yAxis.setLabelRotationAngle(60f);
        yAxis.setValueFormatter(formatterEmo);

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
                    emoMap.put(i, getEmotions(c.getInt(emotion_db)));

                        dateMap.put(i, c.getString(time_db));
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

        timeStart = (EditText) findViewById(R.id.editTextTime);
        String selectedStartString = timeStart.getText().toString();

        timeStop = (EditText) findViewById(R.id.editTextTime2);
        String selectedStopString = timeStop.getText().toString();

        try {
            DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
            Date selectedStart = formatter.parse(selectedStartString);
            Date selectedStop = formatter.parse(selectedStopString);
            Log.d(TAG, "START TIME: " + selectedStartString);
            Log.d(TAG, "STOP TIME: " + selectedStopString);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "Неверно введено начальное время.");
            showError("Неверно введено время.");
            return;
        }

        if (selectedStartString.isEmpty())
        {
            Log.d(TAG, "Неверно введено начальное время.");
            showError("Неверно введено начальное время.");
            return;
        }
        else if (selectedStopString.isEmpty()) {
            Log.d(TAG, "Неверно введено конечное время.");
            showError("Неверно введено конечное время.");
            return;
        }

        double mathExpectation = 0;

        for (int i = 0; i < 6; i++)
        {
            float iProbability = onClickQuery(selectedStartString, selectedStopString, i);
            mathExpectation += iProbability*(i);
            Log.d(TAG, "Math expectation: " + mathExpectation + " " + iProbability + " " + i);
        }
        
        createOneButtonAlertDialog(selectedStartString, selectedStopString, mathExpectation);
    }

    private void createOneButtonAlertDialog(String startTime, String stopTime, double mathExpectation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticActivity.this);

        builder.setTitle("Статистика настроения");
        int result = (int) Math.round(mathExpectation);
        String resultEmotion = getEmotions(result);
        if (resultEmotion == "" || result < 0 || result > 6)
        {
            builder.setMessage("Недостаточно данных или данные некоректны.");
        }
        else {
            builder.setMessage("C " + startTime + " до " + stopTime + " скорее всего ваше настроение будет: \n" + resultEmotion);
        }

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });

        builder.show();
    }

    private void showMessage(String textInMessage) {
        Toast.makeText(getApplicationContext(), textInMessage, Toast.LENGTH_LONG).show();
    }

    private void showError(String errorText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticActivity.this);

        builder.setTitle("Ошибка");
        builder.setMessage(errorText);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });

        builder.show();
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
    public float onClickQuery(String startTime, String stopTime, Integer EmotionID) {
        dbHelper = new DBHelper(this);

        Cursor c = null;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Connected EmotionDB: --- ");

        String selection;
        Long count = DatabaseUtils.queryNumEntries(db, "EmotionDB",
                "emotion =? AND time between ? AND ?", new String[]{String.valueOf(EmotionID),startTime,stopTime});


        Long allCount = DatabaseUtils.queryNumEntries(db, "EmotionDB",
                "time between ? AND ?", new String[]{startTime,stopTime});

        Log.d(LOG_TAG, "NUMBER OF DATA IS: " + count);
        Log.d(LOG_TAG, "NUMBER OF ALL DATA IS: " + allCount);

        if (allCount == 0)
        {
            Log.d(TAG, "Нет подходящих данных в бд");
            return 0;
        }

        float probability;
        probability = (float)count / allCount;
        Log.d(LOG_TAG, "PROBABILITY: " + probability + " " + count + " " + allCount + " " + count / allCount);

        dbHelper.close();

        SetChart();
        return probability;
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

    public String getEmotions(Integer code) {
        if (code == 0)
        {
            return Emotions.BEST.decode;
        }
        else if (code == 1)
        {
            return Emotions.PERFECT.decode;
        }
        else if (code == 2)
        {
            return Emotions.GOOD.decode;
        }
        else if (code == 3)
        {
            return Emotions.NORMAL.decode;
        }
        else if (code == 4)
        {
            return Emotions.SOSO.decode;
        }
        else if (code == 5)
        {
            return Emotions.BAD.decode;
        }
        else
        {
            return "";
        }
    }

}