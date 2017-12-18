package com.hengweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liushengjie on 2017/8/15.
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("hourly_forecast")
    public List<HourlyForecast> hourlyForecastList;

}
