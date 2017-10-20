package com.hengweather.android.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hengweather.android.BaseActivity;
import com.hengweather.android.R;

public class ThankActivity extends BaseActivity {

    public Toolbar oToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank);

        oToolbar = (Toolbar) findViewById(R.id.toolbar);
        oToolbar.setTitle(R.string.thank);
        setSupportActionBar(oToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        oToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
