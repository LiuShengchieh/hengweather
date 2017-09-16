package com.hengweather.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class aboutActivity extends AppCompatActivity {

    public Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aToolbar = (Toolbar) findViewById(R.id.toolbar);
        aToolbar.setTitle("关于");
        setSupportActionBar(aToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView adviceText = (TextView) findViewById(R.id.advice);
        TextView openText = (TextView) findViewById(R.id.open_source_component);
        TextView contactText = (TextView) findViewById(R.id.contact);

        adviceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(aboutActivity.this, "advice", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("mailto: liushengchieh@gmail.com");
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(emailIntent);
            }
        });

        openText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(aboutActivity.this, "open source component", Toast.LENGTH_SHORT).show();
                Intent openIntent = new Intent(aboutActivity.this, OpenSourceComponentActivity.class);
                startActivity(openIntent);
            }
        });

        contactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(aboutActivity.this, "contact", Toast.LENGTH_SHORT).show();
                Intent aboutIntent = new Intent(Intent.ACTION_VIEW);
                aboutIntent.setData(Uri.parse("https://liushengchieh.github.io/about/"));
                startActivity(aboutIntent);
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
