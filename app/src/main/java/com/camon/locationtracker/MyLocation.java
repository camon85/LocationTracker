package com.camon.locationtracker;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by camon on 2015-10-07.
 */
public class MyLocation extends RealmObject {
    @PrimaryKey
    private long time;

    private double lat;

    private double lng;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
