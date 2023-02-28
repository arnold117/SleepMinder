package com.arnold.sleepminder.lib.recorders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;

import androidx.core.app.ActivityCompat;

import com.arnold.sleepminder.lib.DebugView;
import com.arnold.sleepminder.lib.detection.FeatureExtractor;
import com.arnold.sleepminder.lib.detection.NoiseModel;

public class AudioRecorder extends Thread {
    private boolean stopped;
    private static AudioRecord audioRecorder;
    private static int N;
    private NoiseModel noiseModel;
    private DebugView debugView;
    private short[] buffer;
    private FeatureExtractor featureExtractor;

    public AudioRecorder(NoiseModel noiseModel, DebugView debugView) {
        this.noiseModel = noiseModel;
        this.debugView = debugView;
        this.featureExtractor = new FeatureExtractor(noiseModel);

        stopped = false;
        audioRecorder = null;
        N = 0;
    }

    public void process(short[] buffer) {
        featureExtractor.update(buffer);

        if (debugView != null) {
            debugView.addPoint2(noiseModel.getLastRLH(), noiseModel.getNormalizedVAR());
            debugView.setLux((float) noiseModel.getLastRMS());
            debugView.post(new Runnable() {
                @Override
                public void run() {
                    debugView.invalidate();
                }
            });
        }
    }

    private void captureAudio() {
        int i = 0;
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
        if (buffer == null) {
            buffer = new short[1600];
        }

        if (N == 0 || (audioRecorder == null || audioRecorder.getState() != AudioRecord.STATE_INITIALIZED)) {
            N = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            if (N < 1600) {
                N = 1600;
            }
            // TODO: Check if this is the right way to do this
            audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N);
            audioRecorder.startRecording();

            while (!stopped) {
                N = audioRecorder.read(buffer, 0, 1600);

                process(buffer);
            }
            audioRecorder.stop();
            audioRecorder.release();
        }
    }

    public void run() {
        captureAudio();
    }

    public void stopRecording() {
        stopped = true;
    }
}
