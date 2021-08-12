package com.appmobiplus.integrador.configuration;

import java.io.Serializable;
import java.util.Set;

public class Config implements Serializable {
    private IntegrationType integrationType;
    private String path;
    private Set<Field> fields;
    private Set<Header> headers;

    protected Config(IntegrationType integrationType,
                  String path,
                  Set<Field> fields,
                  Set<Header> headers) {
        this.integrationType = integrationType;
        this.path = path;
        this.fields = fields;
        this.headers = headers;
    }

    public IntegrationType getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(IntegrationType integrationType) {
        this.integrationType = integrationType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<Field> getFields() {
        return fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    public Set<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<Header> headers) {
        this.headers = headers;
    }
}
