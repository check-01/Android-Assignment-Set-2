package com.example.weathertrack;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weathertrack.data.WeatherEntity;
import com.example.weathertrack.viewmodel.WeatherViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class WeeklySummaryActivity extends AppCompatActivity {
    private WeatherViewModel viewModel;
    private LineChart lineChart;
    private ListView listView;
    private List<WeatherEntity> data = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private List<String> displayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_summary);

        lineChart = findViewById(R.id.lineChart);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        viewModel.getWeeklyWeather().observe(this, weatherList -> {
            data.clear();
            displayList.clear();
            if (weatherList != null) {
                data.addAll(weatherList);
                for (WeatherEntity w : weatherList) {
                    displayList.add(w.date + ": " + w.temperature + "°C");
                }
                adapter.notifyDataSetChanged();
                drawLineChart(weatherList);
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            WeatherEntity w = data.get(position);
            String detail = "Date: " + w.date + "\nTemp: " + w.temperature + "°C\nHumidity: " + w.humidity + "%\nCondition: " + w.condition;
            Toast.makeText(this, detail, Toast.LENGTH_LONG).show();
        });

        viewModel.loadWeeklyWeather();
    }

    private void drawLineChart(List<WeatherEntity> weatherList) {
        List<Entry> entries = new ArrayList<>();
        final List<String> xLabels = new ArrayList<>();
        for (int i = 0; i < weatherList.size(); i++) {
            entries.add(new Entry(i, weatherList.get(i).temperature));
            xLabels.add(weatherList.get(i).date);
        }
        LineDataSet dataSet = new LineDataSet(entries, "Temperature (°C)");
        dataSet.setColor(Color.BLUE);                 // Line color
        dataSet.setCircleColor(Color.RED);            // Data point color
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);       // Value label color
        dataSet.setDrawValues(true);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // === UI IMPROVEMENTS FOR VISIBILITY ===
        lineChart.setBackgroundColor(Color.WHITE);            // Chart background
        lineChart.setDrawGridBackground(false);

        // X Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setLabelRotationAngle(-45);
        xAxis.setTextColor(Color.BLACK);                      // X axis label color
        xAxis.setAxisLineColor(Color.BLACK);                  // X axis line color

        // Y Axis (Left)
        lineChart.getAxisLeft().setTextColor(Color.BLACK);    // Y axis label color
        lineChart.getAxisLeft().setAxisLineColor(Color.BLACK);// Y axis line color

        // Y Axis (Right)
        lineChart.getAxisRight().setTextColor(Color.BLACK);
        lineChart.getAxisRight().setAxisLineColor(Color.BLACK);
        lineChart.getAxisRight().setEnabled(false);           // Hide right axis if not needed

        // Legend and Description
        lineChart.getLegend().setTextColor(Color.BLACK);
        Description desc = new Description();
        desc.setText("Temperature Trend (7 days)");
        desc.setTextSize(12f);
        desc.setTextColor(Color.DKGRAY);
        lineChart.setDescription(desc);

        lineChart.animateY(1000);
        lineChart.invalidate();

        // Click on a data point to show details
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int) e.getX();
                if (index >= 0 && index < data.size()) {
                    WeatherEntity w = data.get(index);
                    String detail = "Date: " + w.date + "\nTemp: " + w.temperature + "°C\nHumidity: " + w.humidity + "%\nCondition: " + w.condition;
                    Toast.makeText(WeeklySummaryActivity.this, detail, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected() {}
        });
    }
}
