package com.arnold.sleepminder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arnold.sleepminder.R;
import com.arnold.sleepminder.lib.charting.charts.CombinedChart;
import com.arnold.sleepminder.lib.charting.charts.HorizontalBarChart;
import com.arnold.sleepminder.lib.charting.charts.PieChart;
import com.arnold.sleepminder.lib.charting.components.Legend;
import com.arnold.sleepminder.lib.charting.data.BarData;
import com.arnold.sleepminder.lib.charting.data.BarDataSet;
import com.arnold.sleepminder.lib.charting.data.BarEntry;
import com.arnold.sleepminder.lib.charting.data.Entry;
import com.arnold.sleepminder.lib.charting.data.PieData;
import com.arnold.sleepminder.lib.charting.data.PieDataSet;
import com.arnold.sleepminder.lib.charting.utils.ColorTemplate;
import com.arnold.sleepminder.storage.FileHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SingleNight extends AppCompatActivity {
    public static String EXTRA_FILE = "file";

    private File file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_single_night);

        String filename = getIntent().getStringExtra(EXTRA_FILE);

        setDateTitle(filename);

        file = new File(filename);

        String content = FileHandler.readFile(file);

        String[] parts = content.split(";");
        String start = parts[0];

        CombinedChart chart = (CombinedChart) findViewById(R.id.chart);

        ArrayList<Entry> lightEntries = new ArrayList<Entry>();
        ArrayList<BarEntry> sleepStageEntries = new ArrayList<BarEntry>();
        ArrayList<BarEntry> sleepEventEntries = new ArrayList<BarEntry>();

        ArrayList<String> xVals = new ArrayList<String>();

        int j = 0;
        int movement = 0;

        // 30 minutes intervals
        int[] intervals = new int[(int)Math.ceil(parts.length/300f)];
        int[] lightIntervals = new int[(int)Math.ceil(parts.length/300f)];

        int sleepEvents = 0;
        int movementEvents = 0;
        int snoreEvents = 0;

        int deep = 0;
        int light = 0;

        int nightLight = 0;
        int dawnLight = 0;
        int dayLight = 0;

        ArrayList<Integer> lightsIntensities = new ArrayList<Integer>();


    }

    private void setupSleepStagesChart(int deep, int light) {
        PieChart chart = findViewById(R.id.sleepStages);

        ArrayList<Entry> pieComp1 = new ArrayList<Entry>();
        Entry cle1 = new Entry(deep, 0);
        pieComp1.add(cle1);
        Entry cle2 = new Entry(light, 1);
        pieComp1.add(cle2);

        PieDataSet pieDataSet = new PieDataSet(pieComp1, "Sleep Stages");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Deep");
        xVals.add("Light");

        PieData pieData = new PieData(xVals, pieDataSet);
        pieData.setValueTextSize(14);
        pieData.setValueTextColor(Color.WHITE);
        chart.setData(pieData);

        Legend pieLegend = chart.getLegend();
        pieLegend.setEnabled(false);

        chart.setUsePercentValues(true);
        chart.setDescription("Sleep Stages");
        chart.setHardwareAccelerationEnabled(true);
        chart.setBackgroundColor(Color.parseColor("#52B19D"));

        chart.setHoleColor(Color.parseColor("#52B09C"));

        chart.invalidate();
    }

    private void setupSleepEventsChart (int sleep, int movement, int snore) {
        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.sleepEvents);

        ArrayList<BarEntry> barComp1 = new ArrayList<BarEntry>();
        BarEntry c1e1 = new BarEntry(sleep, 0);
        barComp1.add(c1e1);
        BarEntry c1e2 = new BarEntry(movement, 1);
        barComp1.add(c1e2);
        BarEntry c1e3 = new BarEntry(snore, 1);
        barComp1.add(c1e3);

        BarDataSet barDataSet = new BarDataSet(barComp1, "Sleep Events");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setDrawValues(false);

        // TODO set up x-axis labels
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("None");
        xVals.add("Movement");
        xVals.add("Snore");

        BarData barData = new BarData(xVals, barDataSet);
        barData.setValueTextSize(14);
        barData.setValueTextColor(Color.WHITE);
        barChart.setData(barData);

        Legend barLegend = barChart.getLegend();
        barLegend.setEnabled(false);

        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setGridBackgroundColor(Color.parseColor("#52B19D"));

        barChart.setDescription("Sleep Events");
        barChart.setHardwareAccelerationEnabled(true);
        barChart.setDrawBorders(false);

        barChart.invalidate();
    }

    private void setupLightChart(int night, int dawn, int day) {
        PieChart pieChart = findViewById(R.id.lightQuality);

        ArrayList<Entry> pieComp1 = new ArrayList<Entry>();
        Entry cle1 = new Entry(night, 0);
        pieComp1.add(cle1);
        Entry cle2 = new Entry(dawn, 1);
        pieComp1.add(cle2);
        Entry cle3 = new Entry(day, 2);
        pieComp1.add(cle3);

        PieDataSet pieDataSet = new PieDataSet(pieComp1, "Light Quality");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Night");
        xVals.add("Dawn");
        xVals.add("Day");

        PieData pieData = new PieData(xVals, pieDataSet);
        pieData.setValueTextSize(14);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);

        Legend pieLegend = pieChart.getLegend();
        pieLegend.setEnabled(false);

        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Light Quality");
        pieChart.setHardwareAccelerationEnabled(true);

        pieChart.setHoleColor(Color.parseColor("#52B09C"));

        pieChart.invalidate();
    }

    @SuppressLint("SimpleDateFormat")
    private void addPoints(String point, int timeShift,
                           int position, String start,
                           ArrayList<String> xVals,
                           ArrayList<Entry> valsComp1) {
        Entry c1e1 = new Entry(Float.parseFloat(point), position);
        valsComp1.add(c1e1);
        long dv = (Long.parseLong(start) + 5 + timeShift)*1000;
        Date df = new java.util.Date(dv);
        xVals.add(new SimpleDateFormat("HH:mm").format(df));
    }

    private void addPoint2(String point, int timeShift,
                           int position, String start,
                           ArrayList<String> xVals,
                           ArrayList<Entry> valsComp1) {
        Entry c1e1 = new Entry(Float.parseFloat(point), position);
        valsComp1.add(c1e1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.night_list_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            startActivity(Intent.createChooser(share, "Share Recording"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SimpleDateFormat")
    private void setDateTitle(String filename) {
        String timestamp = filename.substring(filename.length()-14, filename.length()-4);
        long dv = Long.parseLong(timestamp)*1000;// its need to be in millisecond
        Date df = new java.util.Date(dv);

        setTitle(new SimpleDateFormat("dd.MM.y HH:mm").format(df));
    }
}
