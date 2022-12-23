package com.app.myweatherapp.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myweatherapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private int layout;
    private ArrayList<HashMap<String,Object>> list ;

    public MyAdapter(int layout, ArrayList<HashMap<String,Object>> list){
        this.layout = layout;
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{ // 有生命週期的東西通常要用 static

        private TextView tvTemp, tvMax, tvMin, tvWindDer, tvWindSpeed, tvStatus; // 老師的作法：在 onCreateView() 裡找到 tvTemp，在 onPostExecute() 裡使用
        private ImageView ivWeather;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            ivWeather = itemView.findViewById(R.id.ivWeather);
        }

        public TextView getTvTemp() {
            return tvTemp;
        }

        public TextView getTvMax() {
            return tvMax;
        }

        public TextView getTvMin() {
            return tvMin;
        }

        public TextView getTvWindDer() {
            return tvWindDer;
        }

        public TextView getTvWindSpeed() {
            return tvWindSpeed;
        }

        public TextView getTvStatus() {
            return tvStatus;
        }

        public ImageView getIvWeather() {
            return ivWeather;
        }
    }

    // 建立並取得ViewHolder物件
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(layout, parent, false);

        return new MyViewHolder(itemView);
    }

    // 在 item layout 上設定資料
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.getTvTemp().setText(list.get(position).get("temp").toString());
        holder.getIvWeather().setImageResource(R.drawable.rain_day);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
