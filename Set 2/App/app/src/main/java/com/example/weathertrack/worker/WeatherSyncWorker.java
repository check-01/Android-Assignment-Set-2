package com.example.weathertrack.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


//import androidx.work.Result;
// environment variable problem
import com.example.weathertrack.repository.WeatherRepository;

public class WeatherSyncWorker extends Worker {
    public WeatherSyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        WeatherRepository repo = new WeatherRepository(getApplicationContext());
        com.example.weathertrack.utils.Result<Void> result = repo.fetchAndSaveWeather(getApplicationContext());
        if (result.error != null) {
            return Result.retry();
        }
        return Result.success();
    }
}
