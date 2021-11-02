package com.appmobiplus.integrador.firebase.databases;

import java.io.Serializable;
import java.util.List;

public class WeatherNextDays implements Serializable {
    private DayWeatherCurrent current;
    private List<DayWeather> daily;
    private double lat;
    private double lon;
    private String timezone;
    private long timezone_offset;

    public DayWeatherCurrent getCurrent() {
        return current;
    }

    public void setCurrent(DayWeatherCurrent current) {
        this.current = current;
    }

    public List<DayWeather> getDaily() {
        return daily;
    }

    public void setDaily(List<DayWeather> daily) {
        this.daily = daily;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(long timezone_offset) {
        this.timezone_offset = timezone_offset;
    }
}
