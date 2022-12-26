package com.app.myweatherapp.ui.main;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.myweatherapp.R;
import com.app.myweatherapp.data.Datasource;
import com.app.myweatherapp.databinding.FragmentMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;
//    View root;     // 我的作法:把 root 宣告在這裡，在 onPostExecute() 裡把 tvTemp 找到並使用
    TextView tvDate, tvTime, tvTemp, tvMax, tvMin, tvWindDer, tvWindSpeed, tvStatus; // 老師的作法：在 onCreateView() 裡找到 tvTemp，在 onPostExecute() 裡使用
    ImageView ivWeather;
    Datasource datasource = new Datasource();

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.v("jena","onCreateView");

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // 下面這些都用 binding.xxx也可以
        tvDate = root.findViewById(R.id.tvDate);
        tvTime = root.findViewById(R.id.tvTime);
        tvTemp = root.findViewById(R.id.tvTemp);
        ivWeather = root.findViewById(R.id.ivWeather);
        tvMax = root.findViewById(R.id.tvMaxTemp);
        tvMin = root.findViewById(R.id.tvMinTemp);
        tvWindDer = root.findViewById(R.id.tvWindDirect);
        tvWindSpeed = root.findViewById(R.id.tvWindSpeed);
        tvStatus = root.findViewById(R.id.tvStatus);

//        開始-------------------------------------------------------------------
//          RecyclerView recyclerView = binding.recyclerView; // 我的第一頁沒 recyclerView 啦
//        結束-------------------------------------------------------------------


//        開始--------------------------------------------------------------------------------
        if (getArguments() != null) {
            int index = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (index) {
//                case 1:
//                    root = inflater.inflate(R.layout.fragment_main, container, false);
//                    break;
                case 2:
                    root = inflater.inflate(R.layout.cityweather, container, false);
                    RecyclerView recyclerView = root.findViewById(R.id.rv_view);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                    recyclerView.setAdapter(new RVAdapter());
                    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                    for(int i = 0; i <100; i++){
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status","Clouds");
                        map.put("temp",18);
                        map.put("direct","N");
                        list.add(map);
                    }
                    recyclerView.setAdapter(new MyAdapter(R.layout.weather_info,list));
                    break;
                case 3:
                    root = inflater.inflate(R.layout.linearlayout, container, false);
                    break;
            }
        }
//        結束---------------------------------------------------------------------------------

//        final TextView textView = binding.sectionLabel;
//        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 建立方法：連結api，取得json格式的天氣資料
    private String fetchWeatherData() {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Taipei,tw&appid=b42d38f45579cbdde04eee8d8120282a");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            byte[] cache = new byte[1024];
            is.read(cache);
//            JSONObject jsonObject = new JSONObject(new String(cache));
            return new String(cache);
        } catch (IOException e) {
            e.printStackTrace();
            return "failed!!";
        }
    }

    // 建立類別：背景同步處理任務
    private class MyAsynTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
//            return datasource.fetchWeatherData();
            return fetchWeatherData();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String result) { // result 為 doInBackground return 回來的東西
            super.onPostExecute(result);
            Log.v("jena", result);

            try {
                JSONObject jObj = new JSONObject(result);

                // 取得當前時間 (格式化)
                long curTime = System.currentTimeMillis();
                String curDateStr = DateFormat.format("EEE. yyyy/MM/dd",curTime).toString();
                String curTimeStr = DateFormat.format("hh:mm",curTime).toString();
                Log.v("jena", "curDateStr =" + curDateStr + ", curTimeStr =" + curTimeStr);

                // 取得位置資訊
                JSONObject jObjCoord = jObj.getJSONObject("coord");
                Double lon = jObjCoord.getDouble("lon");
                Double lat = jObjCoord.getDouble("lat");
                Log.v("jena", "lon=" + lon + ", lat=" + lat);

                // 取得天氣資訊
                JSONObject jObjWeather = jObj.getJSONArray("weather").getJSONObject(0);
                String mainWeather = jObjWeather.getString("main");
                String descWeather = jObjWeather.getString("description");
                Log.v("jena", "mainWeather=" + mainWeather + ", descWeather=" + descWeather);

                // 取得溫度資訊
                JSONObject jObjMain = jObj.getJSONObject("main");
                Double temp = jObjMain.getDouble("temp")-273.15;
                Double minTemp = jObjMain.getDouble("temp_min")-273.15;
                Double maxTemp = jObjMain.getDouble("temp_max")-273.15;
                Log.v("jena", "temp=" + temp + ", minTemp=" + minTemp + ", maxTemp=" + maxTemp);

                // 取得風速風向資訊
                JSONObject jObjWind = jObj.getJSONObject("wind");
                Double windSpeed = jObjWind.getDouble("speed");
                int windDeg = jObjWind.getInt("deg");
                Log.v("jena", "windSpeed=" + windSpeed + ", windDeg=" + windDeg);

                // 顯示資訊
                tvDate.setText(curDateStr);
                tvTime.setText(curTimeStr);
                tvMax.setText(String.format("Max: %2.0f℃",maxTemp));
                tvMin.setText(String.format("Min: %2.0f℃",minTemp));
                tvTemp.setText(String.format("%2.0f°",temp));
                tvWindSpeed.setText(String.format("Wind Speed: %2.0fkm/h",windSpeed));
                tvStatus.setText(descWeather);
                switch (mainWeather){
                    case "Clouds" :
                        ivWeather.setImageResource(R.drawable.partly_cloudy_day);
                        break;
                    case "Rain":
                        ivWeather.setImageResource(R.drawable.rain_day);
                        break;
                    case "Clear":
                        ivWeather.setImageResource(R.drawable.clear_day);
                        break;
                }
                if(windDeg>=45&&windDeg<135){
                    tvWindDer.setText("Wind Direct: East");
                }else if (windDeg>=135&&windDeg<225){
                    tvWindDer.setText("Wind Direct: South");
                }else if (windDeg>=225&&windDeg<315){
                    tvWindDer.setText("Wind Direct: West");
                }else{
                    tvWindDer.setText("Wind Direct: North");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("jena","onViewCreated");
        new MyAsynTask().execute();
    }

//    結束----------------------------------------------------------------------------------------
}