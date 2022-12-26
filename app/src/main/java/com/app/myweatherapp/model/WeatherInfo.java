package com.app.myweatherapp.model;

import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherInfo {

    String curDate, curTime, mainWeather, descWeather, temp, minTemp, maxTemp;

    public WeatherInfo(){

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

    private void getDataFromJson() throws JSONException {
        JSONObject jObj = new JSONObject(fetchWeatherData());

        // 取得當前時間 (格式化)
        long current = System.currentTimeMillis();
        String curDate = DateFormat.format("EEE. yyyy/MM/dd", current).toString();
        String curTime = DateFormat.format("hh:mm", current).toString();
        Log.v("jena", "curDate =" + curDate + ", curTime =" + curTime);

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
    }

}
