package com.arnold.sleepminder.activities.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arnold.sleepminder.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NightListAdapter extends ArrayAdapter<File> {
    public NightListAdapter(@NonNull Context context, int resource, @NonNull List<File> objects) {
        super(context, resource, objects);
    }

    public NightListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @SuppressLint({"SimpleDateFormat", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.listrow_nightlist, null);
        }
        File night = getItem(position);
        if (night != null) {
            TextView tt = v.findViewById(R.id.recordingStartDateTime);

            if (tt != null) {
                String filename = night.getName();
                String timestamp = filename.substring(10, 20);
                long dv = Long.parseLong(timestamp)*1000;
                Date df = new java.util.Date(dv);
                tt.setText(new SimpleDateFormat("dd.MM.y HH:mm").format(df));
            }
        }

        return v;
    }
}
