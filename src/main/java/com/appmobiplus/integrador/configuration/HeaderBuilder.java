package com.appmobiplus.integrador.configuration;

public class HeaderBuilder {
    private String key;
    private String value;

    public HeaderBuilder(String key,
                         String value) {
        this.key = key;
        this.value = value;
    }

    public HeaderBuilder() {

    }

    public static HeaderBuilder get() {
        return new HeaderBuilder();
    }

    public HeaderBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public HeaderBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public Header build() {
        return new Header(key, value);
    }
}
