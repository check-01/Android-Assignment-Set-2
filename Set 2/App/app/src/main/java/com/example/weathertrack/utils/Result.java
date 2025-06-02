package com.example.weathertrack.utils;

public class Result<T>
{
    public T data;
    public String error; // null if success

    public Result(T data)
    {
        this.data = data;
    }
    public Result(String error)
    {
        this.error = error;
    }
}
