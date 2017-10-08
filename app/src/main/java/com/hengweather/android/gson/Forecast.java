package com.hengweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liushengjie on 2017/8/15.
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {

        public String max;

        public String min;
    }

    public class More {

        @SerializedName("txt_d")
        public String info;

        @SerializedName("code_d")
        public int infocode;
    }
}
