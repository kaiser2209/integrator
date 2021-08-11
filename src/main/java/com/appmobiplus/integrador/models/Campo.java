package com.appmobiplus.integrador.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Campo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String originalName;
    private String newName;
    private int initialPos;
    private int finalPos;
    private boolean priceField;
    private int decimalPoint;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public int getInitialPos() {
        return initialPos;
    }

    public void setInitialPos(int initialPos) {
        this.initialPos = initialPos;
    }

    public int getFinalPos() {
        return finalPos;
    }

    public void setFinalPos(int finalPos) {
        this.finalPos = finalPos;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "config_id", nullable = false)
    private Config config;

    public boolean isPriceField() {
        return priceField;
    }

    public void setPriceField(boolean priceField) {
        this.priceField = priceField;
    }

    public int getDecimalPoint() {
        return decimalPoint;
    }

    public void setDecimalPoint(int decimalPoint) {
        this.decimalPoint = decimalPoint;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
