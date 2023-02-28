package com.arnold.sleepminder.lib;

import android.view.View;

public interface DebugView {
    void addPoint2(Double x, Double y);
    void setLux(Float lux);
    void invalidate();
    boolean post(Runnable r);
}
