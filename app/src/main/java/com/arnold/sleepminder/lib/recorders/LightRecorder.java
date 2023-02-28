package com.arnold.sleepminder.lib.recorders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightRecorder {
    private Float lux;
    private SensorEventListener listener;
    private Context context;

    public LightRecorder() {
        lux = null;
        listener = null;
        context = null;
    }

    public Float getLux() {
        return lux;
    }

    private boolean registerSensorListener(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            return false;
        } else {
            if (listener != null) {
                sensorManager.unregisterListener(listener);
            }

            listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    lux = event.values[0];
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            return sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public boolean start(Context context) {
        this.context = context;

        context.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        registerSensorListener(context);
                                    }
                                },
                                1000);
                    }
                }
                , new IntentFilter(Intent.ACTION_SCREEN_OFF));

        return registerSensorListener(context);
    }

    public void stop() {
        lux = null;
        if (listener != null) {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(listener);
            listener = null;
        }
    }
}
