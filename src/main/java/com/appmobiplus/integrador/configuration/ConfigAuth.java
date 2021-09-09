package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.thymeleaf.util.ArrayUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public class ConfigAuth implements Serializable {
    private String path;
    private HttpMethod method;
    private Map<String, String> bodyParameters;
    private String[] fieldsUsedInAuth;
    private String authField;
    private MediaType authBodyType;
    private String authJson;

    public ConfigAuth(String path,
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getBodyParameters() {
        return bodyParameters;
    }

    public void setBodyParameters(Map<String, String> bodyParameters) {
        this.bodyParameters = bodyParameters;
    }

    public String[] getFieldsUsedInAuth() {
        return fieldsUsedInAuth;
    }

    public void setFieldsUsedInAuth(String[] fieldsUsedInAuth) {
        this.fieldsUsedInAuth = fieldsUsedInAuth;
    }

    public String getAuthField() {
        return authField;
    }

    public void setAuthField(String authField) {
        this.authField = authField;
    }

    public MediaType getAuthBodyType() {
        return authBodyType;
    }

    public void setAuthBodyType(MediaType authBodyType) {
        this.authBodyType = authBodyType;
    }

    public String getAuthJson() {
        return authJson;
    }

    public void setAuthJson(String authJson) {
        this.authJson = authJson;
    }

    public String toString() {
        return "[Path= " + this.path + "\n" +
                "Method: " + this.method + "\n" +
                "BodyParameters: " + this.bodyParameters.toString() + "\n" +
                "FieldsUsedInAuth: " + Arrays.toString(this.fieldsUsedInAuth) + "\n" +
                "AuthField: " + this.authField + "]";
    }
}
