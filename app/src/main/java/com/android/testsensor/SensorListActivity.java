package com.android.testsensor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SensorListActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private ListView list_view;
    private View layout_empty;
    private List<Sensor> allSensors;
    private BaseAdapter sensorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);
        layout_empty = findViewById(R.id.layout_empty);
        list_view = findViewById(R.id.list_view);

        sensorAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return allSensors == null ? 0 : allSensors.size();
            }

            @Override
            public Object getItem(int position) {
                return allSensors == null ? null : allSensors.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = View.inflate(SensorListActivity.this, R.layout.item_sensor, null);
                    holder = new ViewHolder();
                    holder.tvSensorName = convertView.findViewById(R.id.tv_sensor_name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Sensor sensor = allSensors.get(position);
                String name = checkSensorName(sensor.getName(), sensor.getType());
                holder.tvSensorName.setText(name);
                return convertView;
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
        };
        list_view.setAdapter(sensorAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sensor sensor = allSensors.get(position);
                int type = sensor.getType();
                Intent intent = new Intent(SensorListActivity.this, SensorDetailActivity.class);
                intent.putExtra(SensorDetailActivity.SENSOR_TYPE, type);
                startActivity(intent);
            }
        });
        getSensor();
    }

    public class ViewHolder {
        TextView tvSensorName;
    }

    private void getSensor() {
        showProgressDialog("正在获取Sensor");
        //获取SensorManager实例
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取当前设备支持的传感器列表
        allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorAdapter.notifyDataSetChanged();
        dismissProgressDialog();
    }

    ProgressDialog progressDialog;

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("提示");
        }
        progressDialog.setMessage(TextUtils.isEmpty(msg) ? "加载中..." : msg);

        if (!isFinishing() && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
