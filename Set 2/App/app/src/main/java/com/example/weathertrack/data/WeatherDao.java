package com.example.weathertrack.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDao
{
    @Insert
    void insert(WeatherEntity weather);

    @Query("SELECT * FROM weather WHERE date >= :startDate ORDER BY date ASC")
    List<WeatherEntity> getWeatherFrom(String startDate);

    @Query("SELECT * FROM weather ORDER BY timestamp DESC LIMIT 1")
    WeatherEntity getLatest();


}
