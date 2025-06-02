package com.example.weathertrack.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weathertrack.data.WeatherEntity;
import com.example.weathertrack.repository.WeatherRepository;
import com.example.weathertrack.utils.Result;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel
{
    private WeatherRepository repo;
    private MutableLiveData<List<WeatherEntity>> weeklyWeather = new MutableLiveData<>();
    private MutableLiveData<WeatherEntity> latestWeather = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public WeatherViewModel(@NonNull Application app)
    {
        super(app);
        repo = new WeatherRepository(app);
    }

    public LiveData<List<WeatherEntity>> getWeeklyWeather()
    {
        return weeklyWeather;
    }

    public LiveData<WeatherEntity> getLatestWeather()
    {
        return latestWeather;
    }

    public LiveData<String> getErrorMessage()
    {
        return errorMessage;
    }

    public void loadWeeklyWeather()
    {
        new Thread(() ->
        {
            Result<List<WeatherEntity>> result = repo.getLast7DaysWeather();
            if (result.error != null)
            {
                errorMessage.postValue(result.error);
            } else
            {
                weeklyWeather.postValue(result.data);
            }
        }).start();
    }

    public void loadLatestWeather()
    {
        new Thread(() ->
        {
            Result<WeatherEntity> result = repo.getLatestWeather();
            if (result.error != null)
            {
                errorMessage.postValue(result.error);
            } else
            {
                latestWeather.postValue(result.data);
            }
        }).start();
    }

    public void refreshWeather(Context context)
    {
        new Thread(() -> {
            Result<Void> result = repo.fetchAndSaveWeather(context);
            if (result.error != null)
            {
                errorMessage.postValue(result.error);
            } else
            {
                loadLatestWeather();
                loadWeeklyWeather();
            }
        }).start();
    }
}
