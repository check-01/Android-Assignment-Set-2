package com.example.weathertrack.network;

import com.example.weathertrack.data.WeatherEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherApiService {
    public WeatherEntity fetchWeather() {
        WeatherEntity weather = new WeatherEntity();
        weather.temperature = 20 + (float)(Math.random() * 10);
        weather.humidity = 60 + (int)(Math.random() * 20);
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Windy"};
        weather.condition = conditions[(int)(Math.random() * conditions.length)];
        weather.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        weather.timestamp = System.currentTimeMillis();
        return weather;
    }
}
