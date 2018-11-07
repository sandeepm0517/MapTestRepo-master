package com.example.sandeepmungara.atmbranchlocator.viewmodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationModel implements Serializable {
    @SerializedName("lat")
    public String lat;
    @SerializedName("lng")
    public String lng;

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLng() {
        return lng;
    }
}
