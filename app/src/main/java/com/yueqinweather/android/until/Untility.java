package com.yueqinweather.android.until;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yueqinweather.android.db.City;
import com.yueqinweather.android.db.County;
import com.yueqinweather.android.db.Province;
import com.yueqinweather.android.gson.AQI;
import com.yueqinweather.android.gson.Suggestion;
import com.yueqinweather.android.gson.Weather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Untility {
    public static Boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response))
        {
            try {
                JSONArray allProvince=new JSONArray(response);
                for(int i=0;i<allProvince.length();i++){
                    JSONObject ProvinceObject=allProvince.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceName(ProvinceObject.getString("name"));
                    province.setCode(ProvinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Boolean hadleCityResponse(String response,int ProvinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCity=new JSONArray(response);
                for(int i=0;i<allCity.length();i++){
                    JSONObject CityObject=allCity.getJSONObject(i);
                    City city=new City();
                    city.setCityName(CityObject.getString("name"));
                    city.setCityCode(CityObject.getInt("id"));
                    city.setProvinceId(ProvinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
                return false;
    }

    public static Boolean hadleCountyResponse(String response,int CityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounty=new JSONArray(response);
                for(int i=0;i<allCounty.length();i++){
                    JSONObject CountyObject=allCounty.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(CountyObject.getString("name"));
                    county.setWeatherId(CountyObject.getString("weather_id"));
                    county.setCityId(CityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather hadleWeatherResponse(String response) {
        return new Gson().fromJson(response, Weather.class);
    }
    public static Suggestion hadleSuggestionResponse(String response) {
        return new Gson().fromJson(response, Suggestion.class);
    }
    public static AQI hadleAqiResponse(String response) {
        return new Gson().fromJson(response, AQI.class);
    }

}

