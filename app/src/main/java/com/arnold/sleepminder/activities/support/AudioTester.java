package com.arnold.sleepminder.activities.support;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arnold.sleepminder.AudioView;

public class AudioTester extends AppCompatActivity {
    AudioView audioView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioView = new AudioView(this);
        setContentView(audioView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioView.stop();
    }
}
