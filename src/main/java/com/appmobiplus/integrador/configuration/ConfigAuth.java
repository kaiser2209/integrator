package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Map;

public class ConfigAuth implements Serializable {
    private String path;
    private HttpMethod methodType;
    private Map<String, String> bodyParameters;

    public ConfigAuth(String path,
                      HttpMethod methodType,
                      Map<String, String> bodyParameters) {
        this.path = path;
        this.methodType = methodType;
        this.bodyParameters = bodyParameters;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethodType() {
        return methodType;
    }

    public void setMethodType(HttpMethod methodType) {
        this.methodType = methodType;
    }

    public Map<String, String> getBodyParameters() {
        return bodyParameters;
    }

    public void setBodyParameters(Map<String, String> bodyParameters) {
        this.bodyParameters = bodyParameters;
    }
}
