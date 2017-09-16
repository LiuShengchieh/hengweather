/*
package com.hengweather.android;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

*
 * Created by liushengjie on 2017/8/29.



public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mDataList;

    public CityAdapter(List<String> dataList) {
        mDataList = dataList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View cityView;
        CardView cardView;
        TextView cityItem;

        public ViewHolder(View view) {
            super(view);
            cityView = view;
            cardView = (CardView) view.findViewById(R.id.cardView);
            cityItem = (TextView) view.findViewById(R.id.city_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        if (mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.cityItem.setText(mDataList.get(position));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
*/
