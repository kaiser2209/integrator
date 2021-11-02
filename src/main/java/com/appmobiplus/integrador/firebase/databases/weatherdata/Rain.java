package com.appmobiplus.integrador.firebase.databases.weatherdata;

import com.google.cloud.firestore.annotation.PropertyName;

import java.io.Serializable;

public class Rain implements Serializable {
    @PropertyName("1h")
    public double _1h;
}
