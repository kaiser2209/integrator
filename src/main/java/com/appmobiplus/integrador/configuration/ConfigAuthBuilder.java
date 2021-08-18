package com.appmobiplus.integrador.configuration;

import java.util.HashMap;
import java.util.Map;

public class ConfigAuthBuilder {
    private String path;
    private MethodType methodType;
    private Map<String, String> bodyParameters = new HashMap<>();

    public ConfigAuthBuilder(MethodType methodType,
                             String path,
                             Map<String, String> bodyParameters) {
        this.path = path;
        this.methodType = methodType;
        this.bodyParameters = bodyParameters;
    }

    public ConfigAuthBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ConfigAuthBuilder setMethodType(MethodType methodType) {
        this.methodType = methodType;
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

    public ConfigAuth build() {
        return new ConfigAuth(path, methodType, bodyParameters);
    }
}
