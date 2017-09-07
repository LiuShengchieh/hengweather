package com.hengweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.hengweather.android.db.City;
import com.hengweather.android.db.County;
import com.hengweather.android.db.Province;
import com.hengweather.android.util.HttpUtil;
import com.hengweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by liushengjie on 2017/8/29.
 */

public class ChooseCity extends BaseActivity {

    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;

    private CityAdapter mCityAdapter;

    private Toolbar toolbarTitle;

    private List<String> dataList = new ArrayList<>();

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    /*
    * 省列表
    * */
    private List<Province> provinceList;

    /*
    * 市列表
    * */
    private List<City> cityList;

    /*
    * 县列表
    * */
    private List<County> countyList;

    /*
    * 选中的省份
    * */
    private Province selectedProvince;

    /*
    * 选中的城市
    * */
    private City selectedCity;

    /*
    * 当前选中的级别
    * */
    private int currentLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_city);
        initView();
        initToolbar();
    }

    private void initToolbar() {
        toolbarTitle = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle.setTitle("切换城市");
        setSupportActionBar(toolbarTitle);
        if (getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_PROVINCE){
                    finish();
                } else if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mCityAdapter = new CityAdapter(dataList);
        recyclerView.setAdapter(mCityAdapter);
        mCityAdapter.setOnItemClickLitener(new CityAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    //  Log.d("TAG", "QueryCity");
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(ChooseCity.this, MainActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    //Toast.makeText(ChooseCity.this, weatherId, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /*
    * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
    * */
    private void queryProvinces() {
        //titleText.setText("中国");
        //backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            mCityAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /*
    * 查询全国所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
    * */
    private void queryCities() {
        //titleText.setText(selectedProvince.getProvinceName());
        //backButton.setVisibility(View.VISIBLE);
        toolbarTitle.setTitle(selectedProvince.getProvinceName());
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).
                find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            mCityAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /*
    * 查询全国所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
    * */
    private void queryCounties() {
        //titleText.setText(selectedCity.getCityName());
        //backButton.setVisibility(View.VISIBLE);
        toolbarTitle.setTitle(selectedCity.getCityName());
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).
                find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            mCityAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /*
    * 根据传入的地址和类型从服务器上查询省市县数据
    * */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseCity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    /*
    * 显示进度对话框
    * */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ChooseCity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /*
    * 关闭进度对话框
    * */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_PROVINCE){
            finish();
        } else if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }
    }
}
