package com.arnold.sleepminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.arnold.sleepminder.lib.DebugView;
import com.arnold.sleepminder.lib.detection.NoiseModel;
import com.arnold.sleepminder.lib.recorders.AudioRecorder;

import java.util.ArrayList;

public class AudioView extends View implements DebugView {

    Paint paint;
    ArrayList<Double> points = null;
    ArrayList<Double[]> points2 = null;
    public static AudioView instance = null;

    public static float lux = 0;
    private int snore = 0;
    private int move = 0;
    private int i = 0;

    ArrayList<Double> rlh = null;
    ArrayList<Double> var = null;
    ArrayList<Double> rms = null;

    private AudioRecorder recorder;
    private NoiseModel noiseModel;

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
//        paint.setAntiAlias(true);
        paint.setTextSize(80);
        points = new ArrayList<>();
        points2 = new ArrayList<>();
        rlh = new ArrayList<>();
        var = new ArrayList<>();
        rms = new ArrayList<>();
        instance = this;

        noiseModel = new NoiseModel();
        recorder = new AudioRecorder(noiseModel, this);
        recorder.start();
    }

    public AudioView(Context context) {
        super(context);
        init();
    }

    public AudioView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void addPoint(Double point) {
        if (points.size() >= 10) {
            points.remove(0);
        }
        points.add(point);
    }

    @Override
    public void addPoint2(Double x, Double y) {
        if (points2.size() >= 10) {
            points2.remove(0);
        }
        Double[] point = {x, y};
        points2.add(point);
    }

    @Override
    public void setLux(Float lux) {
        AudioView.lux = lux;
    }

    public void addRMS(Double point) {
        if (rms.size() >= 300) {
            rms.remove(0);
        }
        rms.add(point);
    }

    public void addRLH(Double point) {
        if (rlh.size() >= 300) {
            rlh.remove(0);
        }
        rlh.add(point);
    }

    public void addVAR(Double point) {
        if (var.size() >= 300) {
            var.remove(0);
        }
        var.add(point);
    }

    protected void drawPoints(Canvas canvas) {
        for (int i = 0; i<rms.size(); i++) {
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(80);
            Double p = rms.get(i);
            canvas.drawCircle(1*4, p.floatValue(), 8, paint);
        }
        for (int i = 0; i<rlh.size(); i++) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(80);
            Double p = rlh.get(i);
            canvas.drawCircle(2*4, p.floatValue(), 8, paint);
        }
        for (int i = 0; i<var.size(); i++) {
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(80);
            Double p = var.get(i);
            canvas.drawCircle(3*4, p.floatValue(), 8, paint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Double[] point : points2) {
            canvas.drawCircle((float) (500 + point[0]), (float) (500 + point[1]), 2, paint);
        }

        if (points2.size() > 0) {
            Double[] curr = points2.get(points2.size() - 1);
            paint.setColor(Color.RED);
            canvas.drawText("RLH: " + curr[0], 100f, 200f, paint);

            paint.setColor(Color.YELLOW);
            canvas.drawText("VAR: " + curr[1], 100f, 300f, paint);

            paint.setColor(Color.BLUE);
            // With doubt, I think this should be rms, not lux
            canvas.drawText("RMS: " + lux, 100f, 400f, paint);

            if (curr[1] > 1) {
                // Filter noise
                if (curr[0] > 2) {
                    snore += 1;
                } else if (lux > 0.5) {
                    move += 1;
                }
            }

            canvas.drawText("Snore: " + snore, 100f, 500f, paint);
            canvas.drawText("Move: " + move, 100f, 600f, paint);

            addRLH((double)(curr[0]*20 + 900));
            addVAR((double)(curr[1]*20 + 900));
            addRMS((double)(lux*20 + 900));
            drawPoints(canvas);
        }
        this.i += 1;
    }
}
