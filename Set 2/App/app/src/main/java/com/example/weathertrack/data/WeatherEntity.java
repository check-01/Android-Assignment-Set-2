package com.example.weathertrack.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather")
public class WeatherEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date;
    public float temperature;
    public int humidity;
    public String condition;
    public long timestamp;
}
