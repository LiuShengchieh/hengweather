package com.hengweather.android.db;

import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;

/**
 * Created by liushengjie on 2017/8/14.
 */

public class County extends DataSupport {

    private int id;

    private String countyName;

    private int weatherId;

    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countryName) {
        this.countyName = countryName;
    }
    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int countryCode) {
        this.weatherId = countryCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
