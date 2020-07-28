package com.yueqinweather.android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yueqinweather.android.gson.AQI;
import com.yueqinweather.android.gson.AllWeather;
import com.yueqinweather.android.gson.Forecast;
import com.yueqinweather.android.gson.Suggestion;
import com.yueqinweather.android.gson.Weather;
import com.yueqinweather.android.until.API;
import com.yueqinweather.android.until.HttpUtil;
import com.yueqinweather.android.until.Untility;

import java.io.IOException;
import java.util.prefs.Preferences;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private LinearLayout forecastLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView titleUpdateTime;
    private TextView weatherInfoText;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private SharedPreferences prefs;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private String weatherId;
    private AllWeather allWeather;
    public WeatherActivity() {
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);
        swipeRefresh=findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary);
        weatherLayout=findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.city_title);
        titleUpdateTime=findViewById(R.id.title_update_time);
        degreeText=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        aqiText=findViewById(R.id.aqi_text);
        pm25Text=findViewById(R.id.pm25_text);
        comfortText=findViewById(R.id.comfort_text);
        carWashText=findViewById(R.id.car_wash_text);
        sportText=findViewById(R.id.sport_text);
        bingPicImg=findViewById(R.id.bing_pic_img);
        drawerLayout=findViewById(R.id.drawer_layout);
        navButton=findViewById(R.id.nav_button);
        allWeather=new AllWeather();
       prefs= getSharedPreferences("weather", 0);
       String bingPic=prefs.getString("bingPic",null);
       if(bingPic!=null){
           Glide.with(this).load(bingPic).into(bingPicImg);
       }else {
           loadBingImg();
       }

        String weatherString=prefs.getString("weather",null);
        String aqiString=prefs.getString("aqi",null);
        String suggestionString=prefs.getString("suggestion",null);
//        if(weatherString!=null&&aqiString!=null&&suggestionString!=null){
//
//            Weather weather= Untility.hadleWeatherResponse(weatherString);
//            AQI aqi=Untility.hadleAqiResponse(aqiString);
//            Suggestion suggestion=Untility.hadleSuggestionResponse(suggestionString);
//            weatherId=prefs.getString("weatherid",null);
//            allWeather.weather=weather;
//            allWeather.aqi=aqi;
//            allWeather.suggestion=suggestion;
//            String s=suggestion.code;
//            showWeatherInfo();
//        }
//        else {
            weatherId=getIntent().getStringExtra("weather_id");
            SharedPreferences.Editor editor=prefs.edit();
            editor.putString("weatherid",weatherId);
            editor.commit();
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
   //     }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    public  void  requestWeather(final String weatherId){
        final String weatherUrl= API.urlWeather+"location="+weatherId+"&key="+API.weatherKey;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override

            public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                }
            });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    final String responseText=response.body().string();

                        final Weather weather = new Gson().fromJson(responseText,Weather.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(weather!=null&&"200".equals(weather.status)){
                                    SharedPreferences.Editor editor=WeatherActivity.this.prefs.edit();
                                    editor.putString("weather",responseText);
                                    editor.commit();
                                    allWeather.weather=Untility.hadleWeatherResponse(responseText);
                                    requesAqi(weatherId);

                                }else {
                                    Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                                }
                            swipeRefresh.setRefreshing(false);

                            }
                        });

            }
        });
        loadBingImg();
    }
    public  void  requesAqi(final String weatherId){
        final String weatherUrl= API.urlAqi+"location="+weatherId+"&key="+API.weatherKey;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override

            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取AQI信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                    final AQI aqi=new Gson().fromJson(responseText,AQI.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(aqi!=null&&"200".equals(aqi.code)){
                                SharedPreferences.Editor editor=WeatherActivity.this.prefs.edit();
                                editor.putString("aqi",responseText);
                                editor.commit();
                                allWeather.aqi=Untility.hadleAqiResponse(responseText);
                                requestSuggestion(weatherId);

                            }else {
                                Toast.makeText(WeatherActivity.this,"获取AQI信息失败",Toast.LENGTH_SHORT).show();
                            }
                            //   swipeRefresh.setRefreshing(false);
                        }
                    });
            }
        });

    }

    public  void  requestSuggestion(final String weatherId){
        final String weatherUrl= API.urlLive+"location="+weatherId+"&key="+API.weatherKey+"&type=0";
        int a;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取生活指数信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                    final Suggestion suggestion = new Gson().fromJson(responseText, Suggestion.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(suggestion!=null&&"200".equals(suggestion.code)){
                                SharedPreferences.Editor editor=WeatherActivity.this.prefs.edit();
                                editor.putString("suggestion",responseText);
                                editor.commit();
                                allWeather.suggestion=Untility.hadleSuggestionResponse(responseText);
                               String s=allWeather.suggestion.daily[15].text;
                                showWeatherInfo();
                            }else {
                                Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                            }
                          swipeRefresh.setRefreshing(false);
                        }
                    });
            }
        });


    }
    private void loadBingImg(){
        HttpUtil.sendOkHttpRequest(API.urlgetphoto, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor= prefs.edit();
                editor.putString("bing_pic",bingPic);
                editor.commit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });

    }
    public  void showWeatherInfo(){
        //String cityName=weather.basic.cityName;
    //    AllWeather weather=allWeather;
        String updateTime=allWeather.weather.updateTime;
        String degree=allWeather.weather.now.temperature+"℃";
        String weatherInfo=allWeather.weather.now.info;
        weatherInfoText.setText(weatherInfo);
        degreeText.setText(degree);
       // titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
    //    forecastLayout.removeAllViews();
        /*
        for(Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dataText=view.findViewById(R.id.date_text);
            TextView infoText=view.findViewById(R.id.info_text);
            TextView maxText=view.findViewById(R.id.max_text);
            TextView minText=view.findViewById(R.id.min_text);
            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }*/

        if(allWeather.aqi!=null){
            aqiText.setText(allWeather.aqi.now.aqi);
            pm25Text.setText(allWeather.aqi.now.pm2p5);
        }
        int size=allWeather.suggestion.daily.length;
        String comfort="过敏指数: "+allWeather.suggestion.daily[11].text;
        String carWash="感冒指数: "+allWeather.suggestion.daily[13].text;
        String sport="运动指数: "+allWeather.suggestion.daily[15].text;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
