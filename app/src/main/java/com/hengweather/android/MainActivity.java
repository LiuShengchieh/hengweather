package com.hengweather.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hengweather.android.ui.SettingActivity;
import com.hengweather.android.ui.aboutActivity;
import com.hengweather.android.util.L;
import com.hengweather.android.util.Utility;
import com.hengweather.android.view.CustomDialog;
import com.zaaach.citypicker.CityPickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    //选择城市
    private static final int REQUEST_CODE_PICK_CITY = 0;

    private String weatherId;

    private Toolbar toolbar;

    private FragmentManager fragmentManager;

    //侧滑菜单
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    private CircleImageView icon_image;
    private CustomDialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    //百度定位
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        //请求位置权限、电话权限、存储权限
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
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            //请求定位
            requestLocation();
        }

        // 初始化侧滑菜单
        initNavigation();
        // 初始化 ToolBar
        initToolBar();

    }

    //请求定位
    private void requestLocation() {
        initLocation(); // 初始化百度地图定位
        mLocationClient.start(); //开始定位
    }

    //定位权限申请结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能让喵呜给您嘘寒问暖喔～",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "咦，出现未知错误啦", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    //初始化百度定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        mLocationClient.setLocOption(option);
    }

    //定位结果
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (location != null) {
                        String city = location.getCity();
                        L.i("城市：", city);
                        String district = location.getDistrict();
                        L.i("区县：", district);
                        if (!TextUtils.isEmpty(district)) {
                            L.i("定位成功", "当前区县为" + district);
                            toShowWeather(district);
                            Toast.makeText(MainActivity.this, "当前位置：" + district, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "定位失败只能看上海的天气啦:-(", Toast.LENGTH_SHORT).show();
                            //定位失败加载默认城市
                            String cityName = "上海";
                            toShowWeather(cityName);
                        }
                        //停止定位
                        mLocationClient.stop();
                    }
                }
            });
        }
    }

    //初始化菜单栏
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //打开侧滑菜单
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    //初始化侧滑菜单
    private void initNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //引入header和menu
        navigationView.inflateHeaderView(R.layout.nav_header);
        navigationView.inflateMenu(R.menu.nav_menu);
        //设置menu的点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //切换城市
                    case R.id.add_city:
                        startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),
                                REQUEST_CODE_PICK_CITY);
                        drawerLayout.closeDrawers();
                        break;
                    //设置
                    case R.id.setting:
                        Intent SettingIntent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(SettingIntent);
                        drawerLayout.closeDrawers();
                        break;
                    //关于
                    case R.id.about:
                        Intent aboutIntent = new Intent(MainActivity.this, aboutActivity.class);
                        startActivity(aboutIntent);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

        //获取头部布局
        View navHeaderView = navigationView.getHeaderView(0);
        //设置圆形头像的点击事件
        icon_image = navHeaderView.findViewById(R.id.icon_image);
        icon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        //读取头像
        Utility.getImageFromShare(this, icon_image);
        //dialog
        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_photo, R.style.Theme_dialog,
                Gravity.BOTTOM, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);
        //打开相机
        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCamera();
            }
        });
        //打开相册
        btn_picture = dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPicture();
            }
        });
        //取消
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //切换城市
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                String cityName = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                toShowWeather(cityName);
            }
        }
        //圆形头像
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                //相机数据
                case CAMERA_REQUEST_CODE:
                    startPhotoZoom(imageUri);
                    break;
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                    }
                    break;
            }
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private Uri imageUri;

    //跳转相机
    private void toCamera() {
        File outputImage = new File(this.getExternalCacheDir(),
                PHOTO_IMAGE_FILE_NAME);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //判断SDK，使用不同的Uri获取方法
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this,
                    "com.hengweather.android.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //裁剪
        intent.putExtra("crop", true);
        //宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //图片质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            icon_image.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存头像
        Utility.putImageToShare(this, icon_image);
    }

    //加载WeatherFragment
    private void toShowWeather(String cityName) {
        weatherId = cityName;
        if (weatherId != null) {
            WeatherFragment weatherFragment = new WeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("weather_id", weatherId);
            weatherFragment.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.myCoor, weatherFragment).commitAllowingStateLoss();
        }
    }

/*
    //连按两次退出app
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        String appName = getString(R.string.app_name);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出" + appName, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                // 淡出动画
                overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
            }
        }
    }*/

    //按一次返回键退到桌面，程序后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
