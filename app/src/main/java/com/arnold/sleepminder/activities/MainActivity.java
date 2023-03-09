package com.arnold.sleepminder.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.arnold.sleepminder.Hooks;
import com.arnold.sleepminder.MyApplication;
import com.arnold.sleepminder.R;
import com.arnold.sleepminder.RecordingService;
import com.arnold.sleepminder.activities.support.AudioTester;
import com.arnold.sleepminder.activities.support.NightListAdapter;
import com.arnold.sleepminder.storage.FileHandler;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private NightListAdapter nightListAdapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // new MyApplication().init();

        if (!isExternalStorageWritable()) {
            new AlertDialog.Builder(this)
                    .setTitle("Caution")
                    .setMessage("External storage is not writable. This app will not work properly.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        // Initialize start/stop button
        try {
            synchronizeStartButtonState(MyApplication.recorder.isRunning());
        }
        catch (NullPointerException exception) {
            Log.e(TAG, "NullPointerException");
            if (MyApplication.recorder == null) {
                Log.w(TAG, "MyApplication.recorder == null");
            }
        }

        // setupNightList();

        findViewById(R.id.toggle_recording).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.recorder.isRunning()) {
                    // stop the tracking service
                    RecordingService.instance.stopSelf();
                    synchronizeStartButtonState(false);
                    Snackbar.make(findViewById(R.id.main_layout), R.string.recording_complete, Snackbar.LENGTH_SHORT).show();

                    Hooks.bind(Hooks.RECORDING_LIST_UPDATE, new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            MainActivity.this.updateNightList();
                            return 1;
                        }
                    });
                } else {
                    Hooks.remove(Hooks.RECORDING_LIST_UPDATE);
                    // start the tracking service
                    Intent trackingIntent = new Intent(MainActivity.this, RecordingService.class);
                    synchronizeStartButtonState(true);
                    Snackbar.make(findViewById(R.id.main_layout), R.string.recording_started, Snackbar.LENGTH_SHORT).show();
                }
                synchronizeStartButtonState(MyApplication.recorder.isRunning());
            }
        });

        findViewById(R.id.start_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, AudioTester.class));
            }
        });
    }

    private void synchronizeStartButtonState(boolean running) {
        ImageView button = (ImageView) findViewById(R.id.toggle_recording);
        if (running) {
            button.setImageResource(R.mipmap.ic_action_stop);
        } else {
            button.setImageResource(R.mipmap.ic_action_play);
        }
    }

    private void setupNightList() {
        ArrayList<File> nights = new ArrayList<>(Arrays.asList(FileHandler.listFiles()));
        nightListAdapter = new NightListAdapter(this, android.R.layout.simple_list_item_1, nights);

        final ListView listView = (ListView) findViewById(R.id.nightsList);
        listView.setAdapter(nightListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = (File) listView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, SingleNight.class);
                intent.putExtra(SingleNight.EXTRA_FILE, file.getAbsolutePath());
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void updateNightList() {
        ArrayList<File> nights = new ArrayList<>(Arrays.asList(FileHandler.listFiles()));
        nightListAdapter.clear();
        nightListAdapter.addAll(nights);
        nightListAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelect(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}