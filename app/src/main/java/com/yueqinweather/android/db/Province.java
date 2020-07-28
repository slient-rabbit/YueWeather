package com.yueqinweather.android.db;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {
    private int id;
    private String ProvinceName;
    private int ProvinceCode;
    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public int getProvinceCode() {
        return ProvinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public void setCode(int ProvinceCode) {
        this.ProvinceCode= ProvinceCode;
    }
}
