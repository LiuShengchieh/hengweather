package com.hengweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hengweather.android.model.Common;
import com.zaaach.citypicker.CityPickerActivity;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_CITY = 0;

    public DrawerLayout drawerLayout;

    private String weatherId;

    private Toolbar toolbar;

    private FragmentManager fragmentManager;

    public LocationClient mLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener());
        //注册监听函数

        /*// 请求位置权限、电话权限、存储权限
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }*/

        initNavigation(); // 初始化侧滑菜单
        initToolBar(); // 初始化 ToolBar
        initLocation(); // 初始化百度地图定位
        mLocationClient.start(); // 开始定位
    }
/*    private void requestLocation() {
        mLocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String weatherId = getIntent().getStringExtra("weather_id");
        if (weatherId != null){
            WeatherFragment weatherFragment = new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("weather_id", weatherId);
            weatherFragment.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.myCoor, weatherFragment).commit();
        }
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open,R.string.drawer_close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    private void initNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_city:
                        //Intent intent = new Intent(MainActivity.this, ChooseCity.class);
                        //Intent intent = new Intent("com.hengweather.android.CHOOSE_CITY");
                        //startActivity(intent);
                        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        //启动
                        startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),
                                REQUEST_CODE_PICK_CITY);

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.setting:
                        //Toast.makeText(MainActivity.this, "尚未开发:-)", Toast.LENGTH_SHORT).show();
                        Intent SettingIntent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(SettingIntent);
                        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.about:
                        //Toast.makeText(MainActivity.this, "尚未开发:-)", Toast.LENGTH_SHORT).show();
                        //Intent aboutIntent = new Intent(Intent.ACTION_VIEW);
                        //aboutIntent.setData(Uri.parse("https://liushengchieh.github.io"));
                        //startActivity(aboutIntent);
                        Intent aboutIntent = new Intent(MainActivity.this, aboutActivity.class);
                        startActivity(aboutIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.exit:
                        finish();
                        //ActivityCollector.finishAll();
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                        break;
                }
                return false;
            }
        });
    }

    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                String cityName = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                queryWeatherCode(cityName);
            }
        }
    }

    private void initLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (location != null) {
                        String city = location.getCity();
                        if (!TextUtils.isEmpty(city)) {
                            String cityName = city.replace("市", "");
                            Log.i("定位成功", "当前城市为" + cityName);
                            queryWeatherCode(cityName);
                            Toast.makeText(MainActivity.this, "当前城市" + cityName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "定位失败只能看上海的天气啦:-)", Toast.LENGTH_SHORT).show();
                            //定位失败加载默认城市
                            String cityName = "上海";
                            queryWeatherCode(cityName);
                        }
                        mLocationClient.stop();
                    }
                }
            });
        }
    }

    /**
     * 转换城市编码
     */
    private void queryWeatherCode(String cityName) {
        weatherId = Common.getCityIdByName(cityName);
        if (weatherId != null) {
            WeatherFragment weatherFragment = new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("weather_id", weatherId);
            weatherFragment.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.myCoor, weatherFragment).commitAllowingStateLoss();
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出MeowWeather", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                // 淡出动画
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                //System.exit(0);
                //Process.killProcess(Process.myPid());
            }
        }
    }

}
