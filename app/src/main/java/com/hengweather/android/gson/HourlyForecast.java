package com.hengweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名：hengweather
 * 包名：  com.hengweather.android.gson
 * 文件名：HourlyForecast
 * Created by liushengjie on 2017/12/4.
 * 描述：小时预报
 */

public class HourlyForecast {

    //时间
    public String date;

    //温度
    public String tmp;

    //天气图片
    @SerializedName("cond")
    public WeatherPicture weatherPicture;

    public class WeatherPicture {

        @SerializedName("code")
        public int infoCode;
    }

}
