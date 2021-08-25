package com.appmobiplus.integrador.configuration;

import com.appmobiplus.integrador.models.Campo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConfigBuilder {
    private IntegrationType integrationType;
    private String path;
    private Set<Field> fields = new HashSet<>();
    private Set<HeaderAuth> headers = new HashSet<>();
    private Map<String, String> parameters = new HashMap<>();
    private boolean hasDelimiter;
    private String delimiter;
    private long fileLastModified;
    private ConfigAuth configAuth;
    private ConfigCadastroProdutos configCadastroProdutos;
    private ConfigCustosProdutos configCustosProdutos;

    public ConfigBuilder(IntegrationType integrationType,
                         String path,
                         Set<Field> fields,
                         Set<HeaderAuth> headers,
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

    public ConfigBuilder setHeaders(Set<HeaderAuth> headers) {
        this.headers = headers;
        return this;
    }

    public ConfigBuilder addHeaders(HeaderAuth header) {
        this.headers.add(header);
        return this;
    }

    public ConfigBuilder setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public ConfigBuilder hasDelimiter(boolean hasDelimiter) {
        this.hasDelimiter = hasDelimiter;
        return this;
    }

    public ConfigBuilder setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public ConfigBuilder setFileLastModified(long fileLastModified) {
        this.fileLastModified = fileLastModified;
        return this;
    }

    public ConfigBuilder setConfigAuth(ConfigAuth configAuth) {
        this.configAuth = configAuth;
        return this;
    }

    public ConfigBuilder setConfigCadastroProdutos(ConfigCadastroProdutos configCadastroProdutos) {
        this.configCadastroProdutos = configCadastroProdutos;
        return this;
    }

    public ConfigBuilder setConfigCustosProdutos(ConfigCustosProdutos configCustosProdutos) {
        this.configCustosProdutos = configCustosProdutos;
        return this;
    }

    public Config build() {
        return new Config(integrationType,
                path,
                fields,
                headers,
                parameters,
                hasDelimiter,
                delimiter,
                fileLastModified,
                configAuth,
                configCadastroProdutos,
                configCustosProdutos);
    }
}
