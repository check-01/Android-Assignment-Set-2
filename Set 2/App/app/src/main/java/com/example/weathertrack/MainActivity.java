package com.example.weathertrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.weathertrack.viewmodel.WeatherViewModel;
import com.example.weathertrack.worker.WeatherSyncWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private WeatherViewModel viewModel;
    private TextView weatherText;
    private Button refreshBtn, weeklyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherText = findViewById(R.id.weatherText);
        refreshBtn = findViewById(R.id.refreshBtn);
        weeklyBtn = findViewById(R.id.weeklyBtn);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        viewModel.getLatestWeather().observe(this, weather -> {
            if (weather != null) {
                weatherText.setText(
                        "Date: " + weather.date + "\n" +
                                "Temp: " + weather.temperature + "°C\n" +
                                "Humidity: " + weather.humidity + "%\n" +
                                "Condition: " + weather.condition
                );
            } else {
                weatherText.setText("No weather data.");
            }
        });

        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        });

        refreshBtn.setOnClickListener(v -> {
            viewModel.refreshWeather(this);
        });

        weeklyBtn.setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, WeeklySummaryActivity.class));
        });

        // Schedule background sync
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                WeatherSyncWorker.class, 6, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "WeatherSync", ExistingPeriodicWorkPolicy.KEEP, workRequest);

        viewModel.loadLatestWeather();
    }
}
