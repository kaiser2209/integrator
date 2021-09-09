package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class ConfigAuthBuilder {
    private String path;
    private HttpMethod method;
    private Map<String, String> bodyParameters = new HashMap<>();
    private String[] fieldsUsedInAuth = new String[0];
    private String authField;
    private MediaType authBodyType;
    private String authJson;

    public ConfigAuthBuilder(String path,
                             HttpMethod method,
                             Map<String, String> bodyParameters,
                             String[] fieldsUsedInAuth,
                             String authField,
                             MediaType authBodyType,
                             String authJson) {
        this.path = path;
        this.method = method;
        this.bodyParameters = bodyParameters;
        this.fieldsUsedInAuth = fieldsUsedInAuth;
        this.authField = authField;
        this.authBodyType = authBodyType;
        this.authJson = authJson;
    }

    public ConfigAuthBuilder() {

    }

    public ConfigAuthBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigAuthBuilder setMethodType(HttpMethod method) {
        this.method = method;
        return this;
    }

    public ConfigAuthBuilder setBodyParameters(Map<String, String> bodyParameters) {
        this.bodyParameters = bodyParameters;
        return this;
    }

    public ConfigAuthBuilder addBodyParameter(String key, String value) {
        this.bodyParameters.put(key, value);
        return this;
    }

    public ConfigAuthBuilder setFieldsUsedInAuth(String[] fieldsUsedInAuth) {
        this.fieldsUsedInAuth = fieldsUsedInAuth;
        return this;
    }

    public ConfigAuthBuilder setAuthField(String authField) {
        this.authField = authField;
        return this;
    }

    public ConfigAuthBuilder setAuthBodyType(MediaType authBodyType) {
        this.authBodyType = authBodyType;
        return this;
    }

    public ConfigAuthBuilder setAuthJson(String authJson) {
        this.authJson = authJson;
        return this;
    }

    public ConfigAuth build() {
        return new ConfigAuth(path, method, bodyParameters, fieldsUsedInAuth, authField, authBodyType, authJson);
    }
}
