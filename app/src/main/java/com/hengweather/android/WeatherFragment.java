package com.hengweather.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengweather.android.gson.Forecast;
import com.hengweather.android.gson.Weather;
import com.hengweather.android.util.HttpUtil;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) context;
            mToolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
        }
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
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
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary); // 下拉刷新进度条的颜色

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
        Log.d(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        String weatherId = getActivity().getIntent().getStringExtra("weather_id");
        if (weatherId != null) {
            requestWeather(weatherId);
        } else {
            Log.d(TAG,"WeatherId is null");
        }
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }


    /*
* 处理并展示 Weather 实体类中的数据
* */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.info;
        cityText.setText(cityName);
        updateText.setText("Update Time: " + updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item,
                    forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String sport = "运动建议：" + weather.suggestion.sport.info;
        String drsg = "穿衣指数：" + weather.suggestion.drsg.info;
        String flu = "感冒指数：" + weather.suggestion.flu.info;
        String uv = "紫外线指数：" + weather.suggestion.uv.info;
        sportText.setText(sport);
        drsgText.setText(drsg);
        fluText.setText(flu);
        uvText.setText(uv);
        weatherLayout.setVisibility(View.VISIBLE);
//        Intent intent = new Intent(this, AutoUpdateService.class);
//        startService(intent);
    }


    /*;
    * 根据天气 id 请求城市天气信息
    * */
    public void requestWeather(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=d851400db09a4107b4259a8bcd54dfa2";
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
                            //Toast.makeText(getActivity(), "天气信息已最新:-)",
                              //      Toast.LENGTH_SHORT).show();
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

}
