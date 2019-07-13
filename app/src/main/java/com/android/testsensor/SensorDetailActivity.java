package com.android.testsensor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SensorDetailActivity extends AppCompatActivity implements SensorEventListener {

    public static final String SENSOR_TYPE = "SENSOR_TYPE";
    private SensorManager sensorManager;
    private LineChart line_chart;
    private Sensor defaultSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        int sensorType = getIntent().getIntExtra(SENSOR_TYPE, -1);
        if (sensorType == -1) {
            finish();
            return;
        }
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        defaultSensor = sensorManager.getDefaultSensor(sensorType);
        line_chart = findViewById(R.id.line_chart);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(checkSensorName(defaultSensor.getName(), defaultSensor.getType()));
        }
        line_chart.setTouchEnabled(false);
        line_chart.getXAxis().setEnabled(true);
        line_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        line_chart.getAxisLeft().setEnabled(true);
        line_chart.getAxisRight().setEnabled(false);
        List<Entry> XEntries = new ArrayList<>();
        List<Entry> YEntries = new ArrayList<>();
        List<Entry> ZEntries = new ArrayList<>();
        LineDataSet XDataset = new LineDataSet(XEntries, "X轴");
        LineDataSet YDataset = new LineDataSet(YEntries, "Y轴");
        LineDataSet ZDataset = new LineDataSet(ZEntries, "Z轴");
        XDataset.setDrawCircles(false);
        YDataset.setDrawCircles(false);
        ZDataset.setDrawCircles(false);
        XDataset.setColor(Color.rgb(255, 0, 0));
        YDataset.setColor(Color.rgb(0, 255, 0));
        ZDataset.setColor(Color.rgb(0, 0, 255));
        LineData lineData = new LineData();
        lineData.addDataSet(XDataset);
        lineData.addDataSet(YDataset);
        lineData.addDataSet(ZDataset);
        line_chart.setData(lineData);

    }

    private String checkSensorName(String sensorName, int type) {
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorName = "加速度传感器(Accelerometer sensor)";
                break;
            case Sensor.TYPE_GYROSCOPE:
                sensorName = "陀螺仪传感器(Gyroscope sensor)";
                break;
            case Sensor.TYPE_LIGHT:
                sensorName = "光线传感器(Light sensor)";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                sensorName = "磁场传感器(Magnetic field sensor)";
                break;
            case Sensor.TYPE_ORIENTATION:
                sensorName = "方向传感器(Orientation sensor)";
                break;
            case Sensor.TYPE_PRESSURE:
                sensorName = "气压传感器(Pressure sensor)";
                break;
            case Sensor.TYPE_PROXIMITY:
                sensorName = "距离传感器(Proximity sensor)";
                break;
            case Sensor.TYPE_TEMPERATURE:
                sensorName = "温度传感器(Temperature sensor)";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                sensorName = "游戏动作传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                sensorName = "地磁旋转矢量传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_GRAVITY:
                sensorName = "重力传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                sensorName = "未校准陀螺仪传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sensorName = "线性加速度传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                sensorName = "未校准磁力传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                sensorName = "湿度传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                sensorName = "旋转矢量传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                sensorName = "特殊动作触发传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_STEP_COUNTER:
                sensorName = "计步传感器(" + sensorName + ")";
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                sensorName = "步行检测传感器(" + sensorName + ")";
                break;
        }
        return sensorName;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sensorManager != null && defaultSensor != null) {
            sensorManager.registerListener(this, defaultSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private int timeIndex = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float values[] = event.values;
        LineData lineData = line_chart.getLineData();
        if (lineData != null) {
            LineDataSet XLineDataSet = (LineDataSet) lineData.getDataSetByLabel("X轴", true);
            LineDataSet YLineDataSet = (LineDataSet) lineData.getDataSetByLabel("Y轴", true);
            LineDataSet ZLineDataSet = (LineDataSet) lineData.getDataSetByLabel("Z轴", true);
            switch (values.length) {
                case 1:
                    XLineDataSet.addEntry(new Entry(timeIndex++, values[0]));
                    break;
                case 2:
                    XLineDataSet.addEntry(new Entry(timeIndex++, values[0]));
                    YLineDataSet.addEntry(new Entry(timeIndex++, values[1]));
                    break;
                case 3:
                    XLineDataSet.addEntry(new Entry(timeIndex++, values[0]));
                    YLineDataSet.addEntry(new Entry(timeIndex++, values[1]));
                    ZLineDataSet.addEntry(new Entry(timeIndex++, values[2]));
                    break;
                default:
                    return;
            }
            if (XLineDataSet.getEntryCount() < 200) {
                line_chart.getXAxis().setAxisMaximum(200);
            } else {
                XLineDataSet.removeFirst();
                YLineDataSet.removeFirst();
                ZLineDataSet.removeFirst();
                line_chart.getXAxis().setAxisMaximum(XLineDataSet.getXMax());

            }
            line_chart.getAxisLeft().setAxisMaximum(XLineDataSet.getYMax());
            line_chart.getAxisLeft().setAxisMinimum(XLineDataSet.getYMin());
            line_chart.getXAxis().setAxisMinimum(XLineDataSet.getXMin());
            line_chart.notifyDataSetChanged();
            line_chart.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
