package com.appmobiplus.integrador.firebase.databases.weatherdata;

import java.io.Serializable;

public class FeelsLike implements Serializable {
    private double day;
    private double eve;
    private double morn;
    private double night;

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }
}
