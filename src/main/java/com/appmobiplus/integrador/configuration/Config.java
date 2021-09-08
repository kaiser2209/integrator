package com.appmobiplus.integrador.configuration;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class Config implements Serializable {
    private IntegrationType integrationType;
    private String path;
    private Map<String, String> parameters;
    private Set<Field> fields;
    private Set<Header> headers;
    private boolean hasDelimiter;
    private String delimiter;
    private long fileLastModified;
    private ConfigAuth configAuth;
    private ConfigCadastroProdutos configCadastroProdutos;
    private ConfigCustosProdutos configCustosProdutos;

    protected Config(IntegrationType integrationType,
                     String path,
                     Set<Field> fields,
                     Set<Header> headers,
                     Map<String, String> parameters,
                     boolean hasDelimiter,
                     String delimiter,
                     long fileLastModified,
                     ConfigAuth configAuth,
                     ConfigCadastroProdutos configCadastroProdutos,
                     ConfigCustosProdutos configCustosProdutos) {
        this.integrationType = integrationType;
        this.path = path;
        this.fields = fields;
        this.headers = headers;
        this.parameters = parameters;
        this.hasDelimiter = hasDelimiter;
        this.delimiter = delimiter;
        this.fileLastModified = fileLastModified;
        this.configAuth = configAuth;
        this.configCadastroProdutos = configCadastroProdutos;
        this.configCustosProdutos = configCustosProdutos;
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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public boolean isHasDelimiter() {
        return hasDelimiter;
    }

    public void setHasDelimiter(boolean hasDelimiter) {
        this.hasDelimiter = hasDelimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public long getFileLastModified() {
        return fileLastModified;
    }

    public void setFileLastModified(long fileLastModified) {
        this.fileLastModified = fileLastModified;
    }

    public ConfigAuth getConfigAuth() {
        return configAuth;
    }

    public void setConfigAuth(ConfigAuth configAuth) {
        this.configAuth = configAuth;
    }

    public ConfigCadastroProdutos getConfigCadastroProdutos() {
        return configCadastroProdutos;
    }

    public void setConfigCadastroProdutos(ConfigCadastroProdutos configCadastroProdutos) {
        this.configCadastroProdutos = configCadastroProdutos;
    }

    public void addHeader(String key, String value) {
        Header h = HeaderBuilder.get()
                .addKeySet(key, value)
                .build();

        this.headers.add(h);
    }

    public ConfigCustosProdutos getConfigCustosProdutos() {
        return configCustosProdutos;
    }

    public void setConfigCustosProdutos(ConfigCustosProdutos configCustosProdutos) {
        this.configCustosProdutos = configCustosProdutos;
    }

    public String getFieldOriginalName(String newName) {
        for(Field f : fields) {
            if(f.getNewName().equals(newName)) {
                return f.getOriginalName();
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "IntegrationType: " + integrationType + "\n" +
                "Path: " + path + "\n" +
                "Fields: " + fields.toString() + "\n" +
                "Headers: " + headers.toString() + "\n" +
                "ConfigAuth: " + ((configAuth != null) ? configAuth.toString() : "Sem ConfigAuth") + "\n" +
                "ConfigCadastroProdutos: " + ((configCadastroProdutos != null) ? configCadastroProdutos.toString() : "Sem ConfigCadastroProdutos") + "\n" +
                "ConfigCustosProdutos: " + ((configCustosProdutos != null) ? configCustosProdutos.toString() : "Sem ConfigCustosProdutos");
    }
}
