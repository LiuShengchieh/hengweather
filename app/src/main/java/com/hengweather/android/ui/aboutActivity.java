package com.hengweather.android.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hengweather.android.BaseActivity;
import com.hengweather.android.R;

public class aboutActivity extends BaseActivity implements View.OnClickListener {

    public Toolbar aToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        aToolbar = (Toolbar) findViewById(R.id.toolbar);
        aToolbar.setTitle("关于");
        setSupportActionBar(aToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //产品建议
        TextView adviceText = (TextView) findViewById(R.id.advice);
        adviceText.setOnClickListener(this);
        //开源组件
        TextView openText = (TextView) findViewById(R.id.open_source_component);
        openText.setOnClickListener(this);
        //联系作者
        TextView contactText = (TextView) findViewById(R.id.contact);
        contactText.setOnClickListener(this);
        //查看源码
        TextView codeText = (TextView) findViewById(R.id.source_code);
        codeText.setOnClickListener(this);
        //分享推荐
        TextView shareText = (TextView) findViewById(R.id.shareApp);
        shareText.setOnClickListener(this);
        //更新app
        TextView updateApp = (TextView) findViewById(R.id.updateApp);
        updateApp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.advice:
                try {
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto: liushengchieh@gmail.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, "产品建议（MeowWeather）");
                    startActivity(data);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.open_source_component:
                try {
                    Intent openIntent = new Intent(aboutActivity.this, OpenSourceComponentActivity.class);
                    startActivity(openIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.contact:
                try {
                    Intent aboutIntent = new Intent(Intent.ACTION_VIEW);
                    aboutIntent.setData(Uri.parse("https://liushengchieh.github.io/about/"));
                    startActivity(aboutIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.source_code:
                try {
                    Intent codeIntent = new Intent(Intent.ACTION_VIEW);
                    codeIntent.setData(Uri.parse("https://github.com/LiuShengchieh/hengweather"));
                    startActivity(codeIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.shareApp:
                try {
                    Intent textIntent = new Intent(Intent.ACTION_SEND);
                    textIntent.setType("text/plain");
                    textIntent.putExtra(Intent.EXTRA_TEXT, "https://www.coolapk.com/apk/com.hengweather.android");
                    startActivity(Intent.createChooser(textIntent, "分享"));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.updateApp:
                String packageN = "com.hengweather.android";
                goToMarket(aboutActivity.this, packageN);
                break;
        }
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

    //打开应用市场
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
