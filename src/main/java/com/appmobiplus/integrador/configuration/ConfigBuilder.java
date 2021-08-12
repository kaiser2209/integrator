package com.appmobiplus.integrador.configuration;

import com.appmobiplus.integrador.models.Campo;

import java.util.HashSet;
import java.util.Set;

public class ConfigBuilder {
    private IntegrationType integrationType;
    private String path;
    private Set<Field> fields = new HashSet<>();
    private Set<Header> headers = new HashSet<>();

    public ConfigBuilder(IntegrationType integrationType,
                         String path,
                         Set<Field> fields,
                         Set<Header> headers) {
        this.integrationType = integrationType;
        this.path = path;
        this.fields = fields;
        this.headers = headers;
    }

    public ConfigBuilder() {

    }

    public static ConfigBuilder get() {
        return new ConfigBuilder();
    }

    public ConfigBuilder setIntegrationType(IntegrationType integrationType) {
        this.integrationType = integrationType;
        return this;
    }

    public ConfigBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigBuilder setFields(Set<Field> fields) {
        this.fields = fields;
        return this;
    }

    public ConfigBuilder addField(Field field) {
        this.fields.add(field);
        return this;
    }

    public ConfigBuilder setHeaders(Set<Header> headers) {
        this.headers = headers;
        return this;
    }

    public ConfigBuilder addHeaders(Header header) {
        this.headers.add(header);
        return this;
    }

    public Config build() {
        return new Config(integrationType, path, fields, headers);
    }
}
