package com.hengweather.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengweather.android.gson.HourlyForecast;
import com.hengweather.android.util.Utility;

import java.util.List;

/**
 * 项目名：hengweather
 * 包名：  com.hengweather.android
 * 文件名：HourlyForecastAdapter
 * Created by liushengjie on 2017/12/4.
 * 描述：三小时预报适配器
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private List<HourlyForecast> mHourlyForecastList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        ImageView iv_hourly;
        TextView tv_tmp;

        public ViewHolder(View view) {
            super(view);
            tv_time = view.findViewById(R.id.tv_time);
            iv_hourly = view.findViewById(R.id.iv_hourly);
            tv_tmp = view.findViewById(R.id.tv_tmp);
        }
    }

    public HourlyForecastAdapter(List<HourlyForecast> hourlyForecastList) {
        mHourlyForecastList = hourlyForecastList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_hour_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyForecast hourlyForecast = mHourlyForecastList.get(position);
        //时间
        String date = hourlyForecast.date;
        String weatherDate = Utility.getHour(date);
        holder.tv_time.setText(weatherDate);
        //三小时预报天气图标
        int infoCode = hourlyForecast.weatherPicture.infoCode;
        switch (infoCode) {
            case 100:
                holder.iv_hourly.setImageResource(R.mipmap.sunny);
                break;
            case 101:
                holder.iv_hourly.setImageResource(R.mipmap.cloudy);
                break;
            case 102:
                holder.iv_hourly.setImageResource(R.mipmap.fewcloudy);
                break;
            case 103:
                holder.iv_hourly.setImageResource(R.mipmap.partlycloudy);
                break;
            case 104:
                holder.iv_hourly.setImageResource(R.mipmap.overcast);
                break;
            case 200:
                holder.iv_hourly.setImageResource(R.mipmap.windy);
                break;
            case 201:
                holder.iv_hourly.setImageResource(R.mipmap.calm);
                break;
            case 202:
                holder.iv_hourly.setImageResource(R.mipmap.lightbreeze);
                break;
            case 203:
                holder.iv_hourly.setImageResource(R.mipmap.moderate);
                break;
            case 204:
                holder.iv_hourly.setImageResource(R.mipmap.freshbreeze);
                break;
            case 205:
                holder.iv_hourly.setImageResource(R.mipmap.strongbreeze);
                break;
            case 206:
                holder.iv_hourly.setImageResource(R.mipmap.highwind);
                break;
            case 207:
                holder.iv_hourly.setImageResource(R.mipmap.gale);
                break;
            case 208:
                holder.iv_hourly.setImageResource(R.mipmap.stronggale);
                break;
            case 209:
                holder.iv_hourly.setImageResource(R.mipmap.storm);
                break;
            case 210:
                holder.iv_hourly.setImageResource(R.mipmap.violentstorm);
                break;
            case 211:
                holder.iv_hourly.setImageResource(R.mipmap.hurricane);
                break;
            case 212:
                holder.iv_hourly.setImageResource(R.mipmap.tornado);
                break;
            case 213:
                holder.iv_hourly.setImageResource(R.mipmap.tropical);
                break;
            case 300:
                holder.iv_hourly.setImageResource(R.mipmap.showerrain);
                break;
            case 301:
                holder.iv_hourly.setImageResource(R.mipmap.heavyshowerrain);
                break;
            case 302:
                holder.iv_hourly.setImageResource(R.mipmap.thundershower);
                break;
            case 303:
                holder.iv_hourly.setImageResource(R.mipmap.heavythunder);
                break;
            case 304:
                holder.iv_hourly.setImageResource(R.mipmap.hail);
                break;
            case 305:
                holder.iv_hourly.setImageResource(R.mipmap.lightrain);
                break;
            case 306:
                holder.iv_hourly.setImageResource(R.mipmap.moderaterain);
                break;
            case 307:
                holder.iv_hourly.setImageResource(R.mipmap.heavyrain);
                break;
            case 308:
                holder.iv_hourly.setImageResource(R.mipmap.extremerain);
                break;
            case 309:
                holder.iv_hourly.setImageResource(R.mipmap.drizzlerain);
                break;
            case 310:
                holder.iv_hourly.setImageResource(R.mipmap.rainstorm);
                break;
            case 311:
                holder.iv_hourly.setImageResource(R.mipmap.heavystorm);
                break;
            case 312:
                holder.iv_hourly.setImageResource(R.mipmap.severestrom);
                break;
            case 313:
                holder.iv_hourly.setImageResource(R.mipmap.freezingrain);
                break;
            case 400:
                holder.iv_hourly.setImageResource(R.mipmap.lightsnow);
                break;
            case 401:
                holder.iv_hourly.setImageResource(R.mipmap.moderatesnow);
                break;
            case 402:
                holder.iv_hourly.setImageResource(R.mipmap.heavysnow);
                break;
            case 403:
                holder.iv_hourly.setImageResource(R.mipmap.snowstorm);
                break;
            case 404:
                holder.iv_hourly.setImageResource(R.mipmap.sleet);
                break;
            case 405:
                holder.iv_hourly.setImageResource(R.mipmap.rainandsnow);
                break;
            case 406:
                holder.iv_hourly.setImageResource(R.mipmap.showersnow);
                break;
            case 407:
                holder.iv_hourly.setImageResource(R.mipmap.snowflurry);
                break;
            case 500:
                holder.iv_hourly.setImageResource(R.mipmap.mist);
                break;
            case 501:
                holder.iv_hourly.setImageResource(R.mipmap.foggy);
                break;
            case 502:
                holder.iv_hourly.setImageResource(R.mipmap.haze);
                break;
            case 503:
                holder.iv_hourly.setImageResource(R.mipmap.sand);
                break;
            case 504:
                holder.iv_hourly.setImageResource(R.mipmap.dust);
                break;
            case 507:
                holder.iv_hourly.setImageResource(R.mipmap.duststorm);
                break;
            case 508:
                holder.iv_hourly.setImageResource(R.mipmap.sandstorm);
                break;
            case 900:
                holder.iv_hourly.setImageResource(R.mipmap.hot);
                break;
            case 901:
                holder.iv_hourly.setImageResource(R.mipmap.cold);
                break;
            case 999:
                holder.iv_hourly.setImageResource(R.mipmap.unknown);
                break;
        }
        //温度
        holder.tv_tmp.setText(hourlyForecast.tmp + "°C");
    }

    @Override
    public int getItemCount() {
        return mHourlyForecastList.size();
    }
}
