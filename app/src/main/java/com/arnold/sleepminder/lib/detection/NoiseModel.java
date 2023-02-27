package com.arnold.sleepminder.lib.detection;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class NoiseModel {
    private List<Double> RMS;
    private List<Double> RLH;
    private List<Double> VAR;

    private int snore = 0;
    private int movement = 0;

    public NoiseModel() {
        RMS = new ArrayList<>();
        RLH = new ArrayList<>();
        VAR = new ArrayList<>();
    }

    public void addRMS(double rms) {
        if (RMS.size() >= 100) {
            RMS.remove(0);
        }
        RMS.add(rms);
    }

    public void addRLH(double rlh) {
        if (RLH.size() >= 100) {
            RLH.remove(0);
        }
        RLH.add(rlh);
    }

    public void addVAR(double var) {
        if (VAR.size() >= 100) {
            VAR.remove(0);
        }
        VAR.add(var);
    }

    private double mea(@NonNull List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    private double std(@NonNull List<Double> values) {
        double mean = mea(values);
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / values.size());
    }

    private double normalize(@NonNull List<Double> values) {
        if (values.size() <= 1) {
            return 0d;
        }
        double mean = mea(values);
        double std = std(values);
        return (values.get(values.size() - 1) - mean) / std;
    }

    public double getNormalizedRMS() {
        return normalize(RMS);
    }

    public double getNormalizedRLH() {
        return normalize(RLH);
    }

    public double getNormalizedVAR() {
        return normalize(VAR);
    }

    public double getLastRMS() {
        if (RMS.size() <= 1) {
            return 0d;
        }
        return RMS.get(RMS.size() - 1);
    }

    public double getLastRLH() {
        if (RLH.size() <= 1) {
            return 0d;
        }
        return RLH.get(RLH.size() - 1);
    }

    public double getLastVAR() {
        if (VAR.size() <= 1) {
            return 0d;
        }
        return VAR.get(VAR.size() - 1);
    }

    public void calculateFrame () {
        if (getLastRLH() > 10) {
            if (getNormalizedRLH() > 2) {
                snore += 1;
                Log.e("event", "snore");
            }
        } else if (getLastRMS() > 15 && getNormalizedVAR() > 0.5d && (getLastRLH() > 1d || getLastRLH() < -1d)) {
            movement += 1;
            Log.e("event", "movement");
        }
    }

    public int getEvent() {
        if (snore > 5) {
            return 1;
        } else if (movement > 1) {
            return 2;
        }
        return 0;
    }

    public int getIntensity() {
        if (getEvent() == 1) {
            return snore;
        } else if (getEvent() == 2) {
            return movement;
        }
        return 0;
    }

    public void resetEvents() {
        snore = 0;
        movement = 0;
    }
}
