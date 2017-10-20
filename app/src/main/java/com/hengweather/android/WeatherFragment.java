package com.hengweather.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengweather.android.gson.Forecast;
import com.hengweather.android.gson.Weather;
import com.hengweather.android.service.AutoUpdateService;
import com.hengweather.android.util.HttpUtil;
import com.hengweather.android.util.L;
import com.hengweather.android.util.StaticClass;
import com.hengweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by liushengjie on 2017/9/1.
 */

public class WeatherFragment extends Fragment {

    private Toolbar mToolbar;

    public SwipeRefreshLayout swipeRefresh;

    private String weatherId;

    private ScrollView weatherLayout;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView sportText;

    private TextView drsgText;

    private TextView fluText;

    private TextView uvText;

    private TextView cityText;

    private TextView updateText;

    public static final String TAG = "WeatherFragment";

    public CardView nowCardView;
    public CardView forecastCardView;
    public CardView aqiCardView;
    public CardView suggestionCardView;

    //天气质量
    private TextView quality;
    private TextView suggestion;

    //天气预报
    private ImageView info_image;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) context;
            mToolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
        }
        L.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        weatherLayout = view.findViewById(R.id.weather_layout);
        cityText = view.findViewById(R.id.city_text);
        updateText = view.findViewById(R.id.update_time);
        degreeText = view.findViewById(R.id.degree_text);
        weatherInfoText = view.findViewById(R.id.weather_info_text);
        forecastLayout = view.findViewById(R.id.forecast_layout);
        aqiText = view.findViewById(R.id.aqi_text);
        pm25Text = view.findViewById(R.id.pm25_text);
        sportText = view.findViewById(R.id.sport_text);
        drsgText = view.findViewById(R.id.drsg_text);
        fluText = view.findViewById(R.id.flu_text);
        uvText = view.findViewById(R.id.uv_text);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        // 下拉刷新进度条的颜色
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        quality = view.findViewById(R.id.quality);
        suggestion = view.findViewById(R.id.suggestion);

        info_image = view.findViewById(R.id.info_image);

        //当前天气
        nowCardView = view.findViewById(R.id.cardView_now);
        //nowCardView.setOnClickListener(this);

        forecastCardView = view.findViewById(R.id.cardView_forecast);
        aqiCardView = view.findViewById(R.id.cardView_aqi);
        suggestionCardView = view.findViewById(R.id.cardView_suggestion);

        weatherId = (String) getArguments().get("weather_id");
        weatherLayout.setVisibility(View.INVISIBLE);
        if (weatherId != null) {
            requestWeather(weatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (weatherId != null) {
                    requestWeather(weatherId);
                    Toast.makeText(getActivity(), "更新成功:-)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "更新失败:-(", Toast.LENGTH_SHORT).show();
                }
            }
        });
        L.d(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.d(TAG, "onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        String weatherId = getActivity().getIntent().getStringExtra("weather_id");
        if (weatherId != null) {
            requestWeather(weatherId);
        } else {
            L.d(TAG,"WeatherId is null");
        }
        L.d(TAG, "onResume");
    }

    /*
* 处理并展示 Weather 实体类中的数据
* */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.more.info;

        // 存储通知栏的天气数据
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("notification", Context.MODE_PRIVATE).edit();
        editor.putString("cityName", cityName);
        editor.putString("degree", degree);
        editor.putString("weatherInfo", weatherInfo);
        editor.apply();

        //当前天气
        cityText.setText(cityName);
        updateText.setText("Update Time - " + updateTime);
        degreeText.setText(degree + "°C");
        weatherInfoText.setText(weatherInfo);

        //三天预报
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item,
                    forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView temperature_text = view.findViewById(R.id.temperature_text);
            //日期
            dateText.setText(forecast.date);
            //天气状况
            infoText.setText(forecast.more.info);
            //最高温最低温
            temperature_text.setText(forecast.temperature.max + "°C" + "/" + forecast.temperature.min + "°C");

            //白天天气状况图标
            ImageView info_image = view.findViewById(R.id.info_image);
            int infoCode = forecast.more.infocode;
            switch (infoCode) {
                case 100:
                    info_image.setBackgroundResource(R.mipmap.sunny);
                    break;
                case 101:
                    info_image.setBackgroundResource(R.mipmap.cloudy);
                    break;
                case 102:
                    info_image.setBackgroundResource(R.mipmap.fewcloudy);
                    break;
                case 103:
                    info_image.setBackgroundResource(R.mipmap.partlycloudy);
                    break;
                case 104:
                    info_image.setBackgroundResource(R.mipmap.overcast);
                    break;
                case 200:
                    info_image.setBackgroundResource(R.mipmap.windy);
                    break;
                case 201:
                    info_image.setBackgroundResource(R.mipmap.calm);
                    break;
                case 202:
                    info_image.setBackgroundResource(R.mipmap.lightbreeze);
                    break;
                case 203:
                    info_image.setBackgroundResource(R.mipmap.moderate);
                    break;
                case 204:
                    info_image.setBackgroundResource(R.mipmap.freshbreeze);
                    break;
                case 205:
                    info_image.setBackgroundResource(R.mipmap.strongbreeze);
                    break;
                case 206:
                    info_image.setBackgroundResource(R.mipmap.highwind);
                    break;
                case 207:
                    info_image.setBackgroundResource(R.mipmap.gale);
                    break;
                case 208:
                    info_image.setBackgroundResource(R.mipmap.stronggale);
                    break;
                case 209:
                    info_image.setBackgroundResource(R.mipmap.storm);
                    break;
                case 210:
                    info_image.setBackgroundResource(R.mipmap.violentstorm);
                    break;
                case 211:
                    info_image.setBackgroundResource(R.mipmap.hurricane);
                    break;
                case 212:
                    info_image.setBackgroundResource(R.mipmap.tornado);
                    break;
                case 213:
                    info_image.setBackgroundResource(R.mipmap.tropical);
                    break;
                case 300:
                    info_image.setBackgroundResource(R.mipmap.showerrain);
                    break;
                case 301:
                    info_image.setBackgroundResource(R.mipmap.heavyshowerrain);
                    break;
                case 302:
                    info_image.setBackgroundResource(R.mipmap.thundershower);
                    break;
                case 303:
                    info_image.setBackgroundResource(R.mipmap.heavythunder);
                    break;
                case 304:
                    info_image.setBackgroundResource(R.mipmap.hail);
                    break;
                case 305:
                    info_image.setBackgroundResource(R.mipmap.lightrain);
                    break;
                case 306:
                    info_image.setBackgroundResource(R.mipmap.moderaterain);
                    break;
                case 307:
                    info_image.setBackgroundResource(R.mipmap.heavyrain);
                    break;
                case 308:
                    info_image.setBackgroundResource(R.mipmap.extremerain);
                    break;
                case 309:
                    info_image.setBackgroundResource(R.mipmap.drizzlerain);
                    break;
                case 310:
                    info_image.setBackgroundResource(R.mipmap.rainstorm);
                    break;
                case 311:
                    info_image.setBackgroundResource(R.mipmap.heavystorm);
                    break;
                case 312:
                    info_image.setBackgroundResource(R.mipmap.severestrom);
                    break;
                case 313:
                    info_image.setBackgroundResource(R.mipmap.freezingrain);
                    break;
                case 400:
                    info_image.setBackgroundResource(R.mipmap.lightsnow);
                    break;
                case 401:
                    info_image.setBackgroundResource(R.mipmap.moderatesnow);
                    break;
                case 402:
                    info_image.setBackgroundResource(R.mipmap.heavysnow);
                    break;
                case 403:
                    info_image.setBackgroundResource(R.mipmap.snowstorm);
                    break;
                case 404:
                    info_image.setBackgroundResource(R.mipmap.sleet);
                    break;
                case 405:
                    info_image.setBackgroundResource(R.mipmap.rainandsnow);
                    break;
                case 406:
                    info_image.setBackgroundResource(R.mipmap.showersnow);
                    break;
                case 407:
                    info_image.setBackgroundResource(R.mipmap.snowflurry);
                    break;
                case 500:
                    info_image.setBackgroundResource(R.mipmap.mist);
                    break;
                case 501:
                    info_image.setBackgroundResource(R.mipmap.foggy);
                    break;
                case 502:
                    info_image.setBackgroundResource(R.mipmap.haze);
                    break;
                case 503:
                    info_image.setBackgroundResource(R.mipmap.sand);
                    break;
                case 504:
                    info_image.setBackgroundResource(R.mipmap.dust);
                    break;
                case 507:
                    info_image.setBackgroundResource(R.mipmap.duststorm);
                    break;
                case 508:
                    info_image.setBackgroundResource(R.mipmap.sandstorm);
                    break;
                case 900:
                    info_image.setBackgroundResource(R.mipmap.hot);
                    break;
                case 901:
                    info_image.setBackgroundResource(R.mipmap.cold);
                    break;
                case 999:
                    info_image.setBackgroundResource(R.mipmap.unknown);
                    break;
            }

            forecastLayout.addView(view);
        }

        //天气质量
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            String pm25Number = weather.aqi.city.pm25;
            int pm25N = Integer.parseInt(pm25Number);
            //判断天气质量
            if (pm25N >= 0 && pm25N <= 35) {
                quality.setText("优");
                suggestion.setText("深呼吸，闭好你的眼睛");
            } else if (pm25N > 35 && pm25N <= 75) {
                quality.setText("良");
                suggestion.setText("铲屎官，正常活动喔");
            } else if (pm25N > 75 && pm25N <= 115) {
                quality.setText("轻污染");
                suggestion.setText("敏感的铲屎官少活动喔");
            } else if (pm25N > 115 && pm25N <= 150) {
                quality.setText("中污染");
                suggestion.setText("减少室外活动，从喵咪做起");
            } else if (pm25N > 150 && pm25N <= 250) {
                quality.setText("重污染");
                suggestion.setText("铲屎官不要出门啦！");
            } else if (pm25N > 250) {
                quality.setText("危险!!!");
                suggestion.setText("喵呜天气已奄奄一息...");
            }
        }

        //生活建议
        String sport = "运动建议：" + weather.suggestion.sport.info;
        String drsg = "穿衣指数：" + weather.suggestion.drsg.info;
        String flu = "感冒指数：" + weather.suggestion.flu.info;
        String uv = "紫外线指数：" + weather.suggestion.uv.info;
        sportText.setText(sport);
        drsgText.setText(drsg);
        fluText.setText(flu);
        uvText.setText(uv);
        weatherLayout.setVisibility(View.VISIBLE);

        // 后台自动更新天气
        Intent intent = new Intent(getActivity(), AutoUpdateService.class);
        getActivity().startService(intent);
    }


    /*;
    * 根据天气 id 请求城市天气信息
    * */
    public void requestWeather(final String weatherId) {

        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" +
                weatherId + "&key=" + StaticClass.HE_WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            // 存储天气数据
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                    getActivity()).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getActivity(), "获取天气信息失败:-(",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

    }

/*    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardView_now:
                shareWeather();
                break;
        }
    }

    private void shareWeather() {

    }*/

}