package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class ConfigAuthBuilder {
    private String path;
    private HttpMethod methodType;
    private HeaderAuth headerAuth;
    private Map<String, String> bodyParameters = new HashMap<>();
    private String[] authFields;

    public ConfigAuthBuilder(String path,
                             HttpMethod methodType,
                             HeaderAuth headerAuth,
                             Map<String, String> bodyParameters,
                             String[] authFields) {
        this.path = path;
        this.methodType = methodType;
        this.headerAuth = headerAuth;
        this.bodyParameters = bodyParameters;
        this.authFields = authFields;
    }

    public ConfigAuthBuilder() {

    }

    public ConfigAuthBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigAuthBuilder setMethodType(HttpMethod methodType) {
        this.methodType = methodType;
        return this;
    }

    public ConfigAuthBuilder setHeaderAuth(HeaderAuth headerAuth) {
        this.headerAuth = headerAuth;
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

    public ConfigAuthBuilder addAuthFields(String[] authFields) {
        this.authFields = authFields;
        return this;
    }

    public ConfigAuth build() {
        return new ConfigAuth(path, methodType, headerAuth, bodyParameters, authFields);
    }
}
