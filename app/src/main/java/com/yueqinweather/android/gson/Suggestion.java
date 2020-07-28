package com.yueqinweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {

    public String code;
    @SerializedName("daily")
    public  Daily []daily;



    public class Daily{
       public String data;
        public String type;
        public  String name;
        public  String level;
        public  String category;
        public  String text;
    }



}
