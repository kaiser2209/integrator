package com.appmobiplus.integrador.configuration;

import java.io.Serializable;

public class Field implements Serializable {
    private String originalName;
    private String newName;
    private int posBegin;
    private int posEnd;
    private boolean currencyField;
    private int decimalPoint;

    protected Field(String originalName,
                 String newName,
                 int posBegin,
                 int posEnd,
                 boolean currencyField,
                 int decimalPoint) {
        this.originalName = originalName;
        this.newName = newName;
        this.posBegin = posBegin;
        this.posEnd = posEnd;
        this.currencyField = currencyField;
        this.decimalPoint = decimalPoint;
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

    public int getPosBegin() {
        return posBegin;
    }

    public void setPosBegin(int posBegin) {
        this.posBegin = posBegin;
    }

    public int getPosEnd() {
        return posEnd;
    }

    public void setPosEnd(int posEnd) {
        this.posEnd = posEnd;
    }

    public boolean isCurrencyField() {
        return currencyField;
    }

    public void setCurrencyField(boolean currencyField) {
        this.currencyField = currencyField;
    }

    public int getDecimalPoint() {
        return decimalPoint;
    }

    public void setDecimalPoint(int decimalPoint) {
        this.decimalPoint = decimalPoint;
    }

    @Override
    public String toString() {
        return "{Names: [Original:" + originalName + ", New:" + newName + "]}";
    }
}
