package com.app.myweatherapp.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Datasource {

    public String fetchWeatherData() {
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




}
