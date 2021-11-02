package com.appmobiplus.integrador.firebase.databases.weatherdata;

import java.io.Serializable;

public class Wind implements Serializable {
    private long deg;
    private double gust;
    private double speed;

    public long getDeg() {
        return deg;
    }

    public void setDeg(long deg) {
        this.deg = deg;
    }

    public double getGust() {
        return gust;
    }

    public void setGust(double gust) {
        this.gust = gust;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
