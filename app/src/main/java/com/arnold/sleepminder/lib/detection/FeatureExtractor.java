package com.arnold.sleepminder.lib.detection;

import android.util.Log;

public class FeatureExtractor {
    private NoiseModel noiseModel;
    private float[] lowFreq;
    private float[] highFreq;

    public FeatureExtractor(NoiseModel noiseModel) {
        this.noiseModel = noiseModel;
    }

    private double calRMS(short[] buffer) {
        double sum = 0;
        for (short value : buffer) {
            sum += Math.pow(value, 2);
        }
        return Math.sqrt(sum / buffer.length);
    }

    private double calRMS(float[] buffer) {
        double sum = 0;
        for (float value : buffer) {
            sum += Math.pow(value, 2);
        }
        return Math.sqrt(sum / buffer.length);
    }

    private double calLowFreqRMS(short[] buffer) {
        lowFreq[0] = 0;
        float a = 0.25f;
        for (int i = 1; i < buffer.length; i++) {
            lowFreq[i] = a * buffer[i] + (1 - a) * lowFreq[i - 1];
        }

        return calRMS(lowFreq);
    }

    private double calHighFreqRMS(short[] buffer) {
        highFreq[0] = 0;
        float a = 0.25f;
        for (int i = 1; i < buffer.length; i++) {
            highFreq[i] = a * buffer[i] + (1 - a) * highFreq[i - 1];
        }

        return calRMS(highFreq);
    }

    private double calRLH(short[] buffer) {
        double lowFreqRMS = calLowFreqRMS(buffer);
        double highFreqRMS = calHighFreqRMS(buffer);
        if (highFreqRMS == 0) return 0;
        if (lowFreqRMS == 0) return 0;
        return lowFreqRMS / highFreqRMS;
    }

    private double calMean(short[] buffer) {
        double sum = 0;
        for (float value : buffer) {
            sum += value;
        }
        return sum / buffer.length;
    }

    private double calVAR(short[] buffer) {
        double mea = calMean(buffer);
        double sum = 0;
        for (float value : buffer) {
            sum += Math.pow(value - mea, 2);
        }
        return sum / buffer.length;
    }

    public void update(short[] buffer) {
        lowFreq = new float[buffer.length];
        highFreq = new float[buffer.length];
        noiseModel.addRLH(calRLH(buffer));
        noiseModel.addRMS(calRMS(buffer));
        noiseModel.addVAR(calVAR(buffer));

        noiseModel.calculateFrame();
    }
}
