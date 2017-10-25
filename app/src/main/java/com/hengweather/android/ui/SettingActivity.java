package com.hengweather.android.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.hengweather.android.BaseActivity;
import com.hengweather.android.MainActivity;
import com.hengweather.android.R;
import com.hengweather.android.util.ShareUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Switch MySwitch;

    public Toolbar toolbar;

    //天气数据
    private String degreeText;
    private String cityName;
    private String weatherInfo;
    private String degree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //标题栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.setting);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //获取天气数据
        SharedPreferences prefs = getSharedPreferences("notification", MODE_PRIVATE);
        cityName = prefs.getString("cityName", "");
        degree = prefs.getString("degree", "");
        weatherInfo = prefs.getString("weatherInfo", "");

        //判断是否下雨
        if (weatherInfo.equals("阵雨") | weatherInfo.equals("强阵雨") | weatherInfo.equals("雷阵雨") |
                weatherInfo.equals("强雷阵雨") | weatherInfo.equals("雷阵雨伴有冰雹")
                |weatherInfo.equals("小雨") | weatherInfo.equals("中雨") |
                weatherInfo.equals("大雨") | weatherInfo.equals("极端降雨") | weatherInfo.equals("毛毛雨/细雨") |
                weatherInfo.equals("暴雨") | weatherInfo.equals("大暴雨") | weatherInfo.equals("特大暴雨")){
            degreeText = "下雨啦，出门记得带伞哟";
        } else {
            //根据温度高低设置不同通知
            int intDegree = Integer.parseInt(degree);
            if (intDegree >= 28) {
                degreeText = "喵呜，谁来帮我擦擦汗？";
            } else if (intDegree >= 24 && intDegree < 28) {
                degreeText = "喵呜，有点小热诶";
            } else if (intDegree >= 21 && intDegree < 24) {
                degreeText = "喵呜，这个温度我给好评";
            } else if (intDegree >= 18 && intDegree < 21) {
                degreeText = "喵呜，凉风习习伴我成长";
            } else if (intDegree >= 15 && intDegree < 18) {
                degreeText = "喵呜，温凉恭谦让";
            } else if (intDegree >= 11 && intDegree < 15) {
                degreeText = "喵呜，毛衣毛衣快上身";
            } else if (intDegree >= 6 && intDegree < 11) {
                degreeText = "喵呜，冷死宝宝了";
            } else if (intDegree < 6) {
                degreeText = "我去，太tm冷了。。。";
            }
        }

        MySwitch = (Switch) findViewById(R.id.MySwitch);
        MySwitch.setOnClickListener(this);

        //获取通知栏天气开关，默认关闭
        boolean isNoti = ShareUtils.getBoolean(this, "isNoti", false);
        MySwitch.setChecked(isNoti);

        if (isNoti) {
            noticeBarWeather(cityName, degree, weatherInfo);
        }

    }

    //打开通知栏天气
    private void noticeBarWeather(String cityName, String degree, String weatherInfo) {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManager manager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(cityName)
                .setContentText(degree + "°C  " + weatherInfo + "    " + degreeText)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.logo))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();
        manager.notify(1, notification);
    }

    //关闭通知栏天气
    private void closeNoti() {
        NotificationManager manager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        manager.cancel(1);
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //通知栏天气开关
            case R.id.MySwitch:
                MySwitch.setSelected(!MySwitch.isSelected());
                ShareUtils.putBoolean(this, "isNoti", MySwitch.isChecked());
                if (MySwitch.isChecked()) {
                    noticeBarWeather(cityName, degree, weatherInfo);
                } else {
                    closeNoti();
                }
                break;
        }
    }

    //退出
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
