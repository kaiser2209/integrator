package com.appmobiplus.integrador.configuration;

public class FieldBuilder {
    private String originalName;
    private String newName;
    private int posBegin;
    private int posEnd;
    private boolean currencyField;
    private int decimalPoint;

    public FieldBuilder(String originalName,
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

    public FieldBuilder() {

    }

    public static FieldBuilder get() {
        return new FieldBuilder();
    }

    public FieldBuilder setOriginalName(String originalName) {
        this.originalName = originalName;
        return this;
    }

    public FieldBuilder setNewName(String newName) {
        this.newName = newName;
        return this;
    }

    public FieldBuilder setPosBegin(int posBegin) {
        this.posBegin = posBegin;
        return this;
    }

    public FieldBuilder setPosEnd(int posEnd) {
        this.posEnd = posEnd;
        return this;
    }

    public FieldBuilder setCurrencyField(boolean currencyField) {
        this.currencyField = currencyField;
        return this;
    }

    public FieldBuilder setDecimalPoint(int decimalPoint) {
        this.decimalPoint = decimalPoint;
        return this;
    }

    public Field build() {
        return new Field(originalName, newName, posBegin, posEnd, currencyField, decimalPoint);
    }
}
