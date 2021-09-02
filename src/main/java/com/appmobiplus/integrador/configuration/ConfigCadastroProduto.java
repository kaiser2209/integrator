package com.appmobiplus.integrador.configuration;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.util.Map;

public class ConfigCadastroProduto implements Serializable {
    private HttpMethod method;
    private String path;
    private Map<String, String> parameters;
    private Map<String, String> header;
    private Map<String, String> body;
    private DataType dataType;

    public ConfigCadastroProduto() {

    }

    public ConfigCadastroProduto(HttpMethod method,
                                 String path,
                                 Map<String, String> parameters,
                                 Map<String, String> header,
                                 Map<String, String> body,
                                 DataType dataType) {

    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String toString() {
        return "{\n" +
                " \"method\":" + method + "\n" +
                " \"path\":" + path + "\n" +
                //" \"parameters\":" + parameters.toString() + "\n" +
                " \"header\":" + header.toString() + "\n" +
                " \"body\":" + body.toString() + "\n" +
                " \"dataType\":" + dataType + "\n" +
                "}";
    }
}
