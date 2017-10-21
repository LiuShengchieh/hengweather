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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengweather.android.BaseActivity;
import com.hengweather.android.R;
import com.hengweather.android.util.Utility;

public class aboutActivity extends BaseActivity implements View.OnClickListener {

    public Toolbar aToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        aToolbar = (Toolbar) findViewById(R.id.toolbar);
        aToolbar.setTitle(R.string.about);
        setSupportActionBar(aToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //获取版本号
        String versionName = Utility.getVersion(this);

        //产品建议
        TextView adviceText = (TextView) findViewById(R.id.advice);
        adviceText.setOnClickListener(this);
        //特别感谢
        TextView openText = (TextView) findViewById(R.id.tv_thank);
        openText.setOnClickListener(this);
        //联系作者
        TextView contactText = (TextView) findViewById(R.id.contact);
        contactText.setOnClickListener(this);
        //分享推荐
        TextView shareText = (TextView) findViewById(R.id.shareApp);
        shareText.setOnClickListener(this);
        //更新app
        TextView updateApp = (TextView) findViewById(R.id.updateApp);
        updateApp.setOnClickListener(this);
        //版本号
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText(versionName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.advice:
                try {
                    Intent data = new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto: shengjiedev@foxmail.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, "产品建议（MeowWeather）");
                    startActivity(data);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_thank:
                try {
                    Intent openIntent = new Intent(aboutActivity.this, ThankActivity.class);
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
            case R.id.shareApp:
                try {
                    Intent textIntent = new Intent(Intent.ACTION_SEND);
                    textIntent.setType("text/plain");
                    textIntent.putExtra(Intent.EXTRA_TEXT, "喵呜天气酷安应用市场" +
                            "下载链接：https://www.coolapk.com/apk/com.hengweather.android");
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
