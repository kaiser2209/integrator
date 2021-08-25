package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;
import org.thymeleaf.util.ArrayUtils;

import java.io.Serializable;
import java.util.Map;

public class ConfigAuth implements Serializable {
    private String path;
    private HttpMethod methodType;
    private Map<String, String> bodyParameters;
    private String[] authFields;

    public ConfigAuth(String path,
                      HttpMethod methodType,
                      Map<String, String> bodyParameters,
                      String[] authFields) {
        this.path = path;
        this.methodType = methodType;
        this.bodyParameters = bodyParameters;
        this.authFields = authFields;
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

    public String[] getAuthFields() {
        return authFields;
    }

    public void setAuthFields(String[] authFields) {
        this.authFields = authFields;
    }

    public String toString() {
        return "[Path= " + this.path +
                this.methodType +
                this.bodyParameters.toString() +
                "]";
    }
}
