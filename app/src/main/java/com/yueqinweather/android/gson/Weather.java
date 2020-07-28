package com.yueqinweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
@SerializedName("code")
    public String status;
    public String updateTime;
    public Now now;

}
