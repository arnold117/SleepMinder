package com.arnold.sleepminder.storage;

import android.util.Log;

import com.arnold.sleepminder.Hooks;
import com.arnold.sleepminder.MyApplication;
import com.arnold.sleepminder.lib.OutputHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler implements OutputHandler {
    private static File getStorageDir() {
        // get the directory for the app's private pictures directory.
        File file = MyApplication.context.getExternalFilesDir("recordings");
        if (file == null || !file.mkdirs()) {
            Log.d("SleepMinder::FileHandler", "Directory not created");
        }
        return file;
    }

    public static File[] listFiles() {
        return getStorageDir().listFiles();
    }

    public static String readFile(File file) {
        // read from txt file
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        }
        catch (IOException e) {
            //TODO: add proper error handling here
        }
        return text.toString();
    }

    @Override
    public void saveData(String data, String identifier) {
        saveData(data, "recording-" + identifier + ".txt");
        Hooks.trigger(Hooks.RECORDING_LIST_UPDATE);
    }

    public static void saveFile(String data, String filename) {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(new File(getStorageDir(), filename), true);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
