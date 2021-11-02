package com.appmobiplus.integrador.firebase.databases.weatherdata;

import java.io.Serializable;

public class Clouds implements Serializable {
    private long all;

    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }
}
