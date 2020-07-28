package com.yueqinweather.android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.yueqinweather.android.gson.Weather;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs=getSharedPreferences("weather", 0);
//        if(prefs.getString("weather",null)!=null){
//            Intent intent=new Intent(this, WeatherActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }
}
