package com.appmobiplus.integrador.configuration;

public class HeaderAuthBuilder {
    private String key;
    private String value;
    private String[] fieldsUsedInValue;

    public HeaderAuthBuilder() {

    }

    public HeaderAuthBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public HeaderAuthBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public HeaderAuthBuilder setFieldsUsedInValue(String[] fieldsUsedInValue) {
        this.fieldsUsedInValue = fieldsUsedInValue;
        return this;
    }

    public HeaderAuth build() {
        return new HeaderAuth(key, value, fieldsUsedInValue);
    }
}
