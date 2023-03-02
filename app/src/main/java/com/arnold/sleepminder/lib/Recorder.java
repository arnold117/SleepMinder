package com.arnold.sleepminder.lib;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import com.arnold.sleepminder.lib.detection.NoiseModel;
import com.arnold.sleepminder.lib.recorders.AudioRecorder;
import com.arnold.sleepminder.lib.recorders.LightRecorder;

public class Recorder {
    private static  String TAG;

    private LightRecorder lightRecorder;
    private AudioRecorder audioRecorder;
    private StringBuilder data;
    private OutputHandler outputHandler;
    private NoiseModel noiseModel;

    private String startTime;
    private boolean running;

    private PowerManager.WakeLock wakeLock;

    public Recorder() {
        TAG = "SleepMinder::Recorder";

        lightRecorder = null;
        audioRecorder = null;
        data = new StringBuilder();
        noiseModel = new NoiseModel();

        startTime = "";
        running = false;
    }

    private void dumpData() {
        // Save data to file
        outputHandler.saveData(data.toString(), startTime);
        // Clear data
        data = new StringBuilder();
    }

    public boolean isRunning() {
        return running;
    }

    public void start(Context context, OutputHandler handler) {
        this.outputHandler = handler;
        this.running = true;

        // Acquire wake lock
        // So that the device doesn't go to sleep while recording
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SleepMinder::WakeLock");
        wakeLock.acquire();

        // Set the current timestamp to the data string
        // And set start time
        startTime = String.valueOf(System.currentTimeMillis() / 1000L);
        data.append(startTime).append(";");

        // Start the light recorder
        lightRecorder = new LightRecorder();
        lightRecorder.start(context);

        // Start the audio recorder
        audioRecorder = new AudioRecorder(noiseModel, null);
        audioRecorder.start();

        // Get current sensors data every 5 seconds
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                synchronized (Recorder.this) {
                    if (lightRecorder == null || audioRecorder == null) {
                        // Already stopped
                        return;
                    }
                    if (lightRecorder.getLux() != null) {
                        data.append(String.valueOf(lightRecorder.getLux().intValue()));
                    } else {
                        data.append(1);
                        Log.d(TAG, "No light sensor data found");
                    }

                    data.append(" ").append(String.valueOf(noiseModel.getEvent()))
                            .append(" ").append(String.valueOf(noiseModel.getIntensity()))
                            .append(";");

                    // Dump data to file if we get 1000 data points
                    // Approximately every 15 minutes.
                    if (data.length() > 1000) {
                        dumpData();
                    }

                    noiseModel.resetEvents();
                    handler1.postDelayed(this, 5000);
                }
            }
        }, 0);
    }

    public void stop(Context context) {
        synchronized (this) {
            if (lightRecorder != null) {
                lightRecorder.stop();
                lightRecorder = null;
            }
            if (audioRecorder != null) {
                audioRecorder.stopRecording();
                audioRecorder = null;
            }
            if (wakeLock != null) {
                wakeLock.release();
                wakeLock = null;
            }
            if (data.length() > 0) {
                dumpData();
            }
            startTime = "";
            running = false;
        }
    }
}
