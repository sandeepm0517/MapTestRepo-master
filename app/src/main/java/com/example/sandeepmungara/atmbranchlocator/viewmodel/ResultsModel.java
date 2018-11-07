package com.example.sandeepmungara.atmbranchlocator.viewmodel;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultsModel implements Serializable {
    @SerializedName("formatted_address")
    public String formatted_address;
    @SerializedName("geometry")
    public Geometry geometry;
    @SerializedName("icon")
    public String icon;
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("rating")
    public String rating;
    @SerializedName("opening_hours")
    public OpeningHours opening_hours;

    public Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOpening_hours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public class OpeningHours {

        public boolean open_now;

//        public OpeningHours(boolean open_now) {
//            this.open_now = open_now;
//        }

        public void setOpen_now(boolean open_now) {
            this.open_now = open_now;
        }

        public boolean getOpen_now() {
            return open_now;
        }
    }
}
