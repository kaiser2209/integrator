package com.appmobiplus.integrador.firebase.databases;

import com.google.cloud.Timestamp;

import java.io.Serializable;
import java.util.List;

public class WeatherBase implements Serializable {
    private String cidade;
    private Timestamp lastUpdated;
    private List<String> idGroups;
    private WeatherNextDays nextDays;
    private WeatherData data;

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<String> getIdGroups() {
        return idGroups;
    }

    public void setIdGroups(List<String> idGroups) {
        this.idGroups = idGroups;
    }

    public WeatherNextDays getNextDays() {
        return nextDays;
    }

    public void setNextDays(WeatherNextDays nextDays) {
        this.nextDays = nextDays;
    }

    public WeatherData getData() {
        return data;
    }

    public void setData(WeatherData data) {
        this.data = data;
    }
}
