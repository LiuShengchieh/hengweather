package com.hengweather.android.gson;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

/**
 * Created by liushengjie on 2017/8/15.
 */

public class Suggestion {

    public Sport sport;

    public Drsg drsg;

    public Flu flu;

    public Uv uv;

    public class Uv {

        @SerializedName("txt")
        public String info;
    }

    public class Flu {

        @SerializedName("txt")
        public String info;
    }

    public class Drsg {

        @SerializedName("txt")
        public String info;
    }


    public class Sport {

        @SerializedName("txt")
        public String info;
    }
}
