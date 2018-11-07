package com.example.sandeepmungara.atmbranchlocator.viewmodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {
    @SerializedName("location")
    public LocationModel location;
    @SerializedName("viewport")
    public ViewportModel viewport;

    public void setLocation(LocationModel location) {
        this.location = location;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setViewport(ViewportModel viewport) {
        this.viewport = viewport;
    }

    public ViewportModel getViewport() {
        return viewport;
    }
}
