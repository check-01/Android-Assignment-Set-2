package com.example.weathertrack.repository;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.example.weathertrack.data.WeatherDao;
import com.example.weathertrack.data.WeatherDatabase;
import com.example.weathertrack.data.WeatherEntity;
import com.example.weathertrack.network.WeatherApiService;
import com.example.weathertrack.utils.NetworkUtils;
import com.example.weathertrack.utils.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherRepository {
    private WeatherDao dao;
    private WeatherApiService api;

    public WeatherRepository(Context context) {
        WeatherDatabase db = WeatherDatabase.getDatabase(context);
        dao = db.weatherDao();
        api = new WeatherApiService();
    }

    public Result<Void> fetchAndSaveWeather(Context context) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            return new Result<>("No internet connection.");
        }
        try {
            WeatherEntity weather = api.fetchWeather();
            dao.insert(weather);
            return new Result<>((Void) null);
        } catch (SQLiteException e) {
            return new Result<>("Database error. Please try again.");
        } catch (Exception e) {
            return new Result<>("Failed to fetch weather data.");
        }
    }

    public Result<List<WeatherEntity>> getLast7DaysWeather() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -6); // include today
            String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
            List<WeatherEntity> list = dao.getWeatherFrom(startDate);
            return new Result<>(list);
        } catch (SQLiteException e) {
            return new Result<>("Database error. Please try again.");
        } catch (Exception e) {
            return new Result<>("Failed to load weather data.");
        }
    }

    public Result<WeatherEntity> getLatestWeather() {
        try {
            WeatherEntity entity = dao.getLatest();
            return new Result<>(entity);
        } catch (SQLiteException e) {
            return new Result<>("Database error. Please try again.");
        } catch (Exception e) {
            return new Result<>("Failed to load weather data.");
        }
    }
}
