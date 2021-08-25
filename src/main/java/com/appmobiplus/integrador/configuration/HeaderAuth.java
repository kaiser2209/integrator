package com.appmobiplus.integrador.configuration;

public class HeaderAuth {
    private String key;
    private String value;
    private String[] fieldsUsedInValue;

    public HeaderAuth(String key, String value, String[] fieldsUsedInValue) {
        this.key = key;
        this.value = value;
        this.fieldsUsedInValue = fieldsUsedInValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getFieldsUsedInValue() {
        return fieldsUsedInValue;
    }

    public void setFieldsUsedInValue(String[] fieldsUsedInValue) {
        this.fieldsUsedInValue = fieldsUsedInValue;
    }
}
