package com.arnold.sleepminder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arnold.sleepminder.R;
import com.arnold.sleepminder.activities.support.NightListAdapter;
import com.arnold.sleepminder.storage.FileHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class NightsList extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nights_list);

        ArrayList<File> nights = new ArrayList<>(Arrays.asList(FileHandler.listFiles()));
        NightListAdapter adapter = new NightListAdapter(this, android.R.layout.simple_list_item_1, nights);

        final ListView listView = (ListView) findViewById(R.id.nightsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = (File) listView.getItemAtPosition(position);

                Intent intent = new Intent(NightsList.this, SingleNight.class);
                intent.putExtra(SingleNight.EXTRA_FILE, file.getAbsolutePath());
                NightsList.this.startActivity(intent);
            }
        });
    }
}
