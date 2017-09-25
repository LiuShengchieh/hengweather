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
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.hengweather.android.BaseActivity;
import com.hengweather.android.MainActivity;
import com.hengweather.android.R;

public class SettingActivity extends BaseActivity {

    public ToggleButton toggleButton;

    public Toolbar sToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sToolbar = (Toolbar) findViewById(R.id.toolbar);
        sToolbar.setTitle("设置");
        setSupportActionBar(sToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("notification", MODE_PRIVATE);
        final String cityName = prefs.getString("cityName", "");
        final String degree = prefs.getString("degree", "");
        final String weatherInfo = prefs.getString("weatherInfo", "");
        toggleButton = (ToggleButton) findViewById(R.id.sw);

        // 进入设置页即自动关闭通知栏天气
        toggleButton.setChecked(false);
        NotificationManager manager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        manager.cancel(1);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    NotificationManager manager = (NotificationManager) getSystemService(
                            NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(cityName)
                            .setContentText(degree + "  " + weatherInfo + "    " + "喵呜，雨水就是我的猫粮")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.logo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.logo))
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setOngoing(true)
                            .build();
                    manager.notify(1, notification);
                } else {
                    NotificationManager manager = (NotificationManager) getSystemService(
                            NOTIFICATION_SERVICE);
                    manager.cancel(1);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
