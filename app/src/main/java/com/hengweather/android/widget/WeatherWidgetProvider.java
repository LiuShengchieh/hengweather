package com.hengweather.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.hengweather.android.R;
import com.hengweather.android.util.L;
import com.hengweather.android.util.ShareUtils;

/**
 * 项目名：hengweather
 * 包名：  com.hengweather.android.widget
 * 文件名：WeatherWidgetProvider
 * Created by liushengjie on 2017/10/21.
 * 描述：桌面小部件
 */

public class WeatherWidgetProvider extends AppWidgetProvider {

    public static final String TAG = "WeatherWidgetProvider";

    public WeatherWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //获取天气数据
        String cityName = ShareUtils.getString(context, "cityName", "");
        String degree = ShareUtils.getString(context, "degree", "");
        String weatherInfo = ShareUtils.getString(context, "weatherInfo", "");
        //获取天气状况代码
        int weatherCode = ShareUtils.getInt(context, "weatherCode", 999);
        //log打印天气码
        String weatherCodeString = Integer.toString(weatherCode);
        L.i(TAG, weatherCodeString);

        //设置remoteviews
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_weather);
        remoteViews.setTextViewText(R.id.tv_city, cityName);
        remoteViews.setTextViewText(R.id.tv_weather, weatherInfo);
        remoteViews.setTextViewText(R.id.tv_temp, degree + "°C");

        //根据天气状况代码显示不同图片
        switch (weatherCode) {
            case 100:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.sunny);
                break;
            case 101:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.cloudy);
                break;
            case 102:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.fewcloudy);
                break;
            case 103:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.partlycloudy);
                break;
            case 104:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.overcast);
                break;
            case 200:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.windy);
                break;
            case 201:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.calm);
                break;
            case 202:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.lightbreeze);
                break;
            case 203:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.moderate);
                break;
            case 204:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.freshbreeze);
                break;
            case 205:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.strongbreeze);
                break;
            case 206:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.highwind);
                break;
            case 207:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.gale);
                break;
            case 208:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.stronggale);
                break;
            case 209:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.storm);
                break;
            case 210:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.violentstorm);
                break;
            case 211:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.hurricane);
                break;
            case 212:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.tornado);
                break;
            case 213:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.tropical);
                break;
            case 300:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.showerrain);
                break;
            case 301:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.heavyshowerrain);
                break;
            case 302:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.thundershower);
                break;
            case 303:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.heavythunder);
                break;
            case 304:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.hail);
                break;
            case 305:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.lightrain);
                break;
            case 306:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.moderaterain);
                break;
            case 307:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.heavyrain);
                break;
            case 308:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.extremerain);
                break;
            case 309:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.drizzlerain);
                break;
            case 310:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.rainstorm);
                break;
            case 311:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.heavystorm);
                break;
            case 312:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.severestrom);
                break;
            case 313:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.freezingrain);
                break;
            case 400:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.lightsnow);
                break;
            case 401:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.moderatesnow);
                break;
            case 402:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.heavysnow);
                break;
            case 403:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.snowstorm);
                break;
            case 404:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.sleet);
                break;
            case 405:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.rainandsnow);
                break;
            case 406:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.showersnow);
                break;
            case 407:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.snowflurry);
                break;
            case 500:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.mist);
                break;
            case 501:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.foggy);
                break;
            case 502:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.haze);
                break;
            case 503:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.sand);
                break;
            case 504:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.dust);
                break;
            case 507:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.duststorm);
                break;
            case 508:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.sandstorm);
                break;
            case 900:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.hot);
                break;
            case 901:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.cold);
                break;
            case 999:
                remoteViews.setImageViewResource(R.id.iv_weather, R.mipmap.unknown);
                break;
        }

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

}
