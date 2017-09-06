package com.hengweather.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingActivity extends BaseActivity {

    public ToggleButton toggleButton;

    public Toolbar sToolbar;

    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sToolbar = (Toolbar) findViewById(R.id.toolbar);
        sToolbar.setTitle("设置");
        setSupportActionBar(sToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

       // drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        SharedPreferences prefs = getSharedPreferences("notification", MODE_PRIVATE);
        final String cityName = prefs.getString("cityName", "");
        final String degree = prefs.getString("degree", "");
        final String weatherInfo = prefs.getString("weatherInfo", "");
        toggleButton = (ToggleButton) findViewById(R.id.sw);
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
                            .setContentText(degree + "  " + weatherInfo + "    " + "喵，亲我一口可好？")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.logo)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.logo))
                            .setContentIntent(pi)
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
