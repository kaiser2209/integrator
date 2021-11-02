package com.appmobiplus.integrador.firebase.databases.weatherdata;

import java.io.Serializable;

public class Main implements Serializable {
    private double feels_like;
    private long grnd_level;
    private long humidity;
    private long pressure;
    private long sea_level;
    private double temp;
    private double temp_max;
    private double temp_min;

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public long getGrnd_level() {
        return grnd_level;
    }

    public void setGrnd_level(long grnd_level) {
        this.grnd_level = grnd_level;
    }

    public long getHumidity() {
        return humidity;
    }

    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public long getPressure() {
        return pressure;
    }

    public void setPressure(long pressure) {
        this.pressure = pressure;
    }

    public long getSea_level() {
        return sea_level;
    }

    public void setSea_level(long sea_level) {
        this.sea_level = sea_level;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }
}
