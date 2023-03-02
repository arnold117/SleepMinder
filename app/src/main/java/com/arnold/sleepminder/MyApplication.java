package com.arnold.sleepminder;

import android.content.Context;

import com.arnold.sleepminder.lib.Recorder;

public class MyApplication extends android.app.Application{
    public Context context;
    public static Recorder recorder;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        recorder = new Recorder();
    }
}
