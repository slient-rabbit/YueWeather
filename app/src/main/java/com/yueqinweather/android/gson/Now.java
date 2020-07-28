package com.yueqinweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("temp")
    public String temperature;

    @SerializedName("text")
    public String info;


}
