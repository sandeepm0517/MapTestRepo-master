package com.example.sandeepmungara.atmbranchlocator.viewmodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ViewportModel implements Serializable {
    @SerializedName("notheast")
    public NorthEast northeast;
    @SerializedName("southwest")
    public Southwest southwest;

    public void setNortheast(NorthEast northeast) {
        this.northeast = northeast;
    }

    public NorthEast getNortheast() {
        return northeast;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public class NorthEast {
        @SerializedName("lat")
        public String lat;
        @SerializedName("lng")
        public String lng;

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLng() {
            return lng;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLat() {
            return lat;
        }
    }

    public class Southwest {
        @SerializedName("lat")
        public String lat;
        @SerializedName("lng")
        public String lng;

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }

}
