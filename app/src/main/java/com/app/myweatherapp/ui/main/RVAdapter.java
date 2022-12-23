package com.app.myweatherapp.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myweatherapp.R;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private int[] mList = {28,30,24,13,12,10,5,7,13};

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTemp;

        // 用來保存UI元件
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTemp = itemView.findViewById(R.id.tvTemp);
        }
    }

    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // get到的Context是MainActivity物件。也可用getActivity()
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_info,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position) {
        Log.v("jena",mList[position]+"");
        Log.v("jena","tvTemp="+holder.tvTemp);
        holder.tvTemp.setText(mList[position]+"");
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }
}
