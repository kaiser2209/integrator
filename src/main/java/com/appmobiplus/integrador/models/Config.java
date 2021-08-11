package com.appmobiplus.integrador.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private IntegrationType integrationType;

    private String path;

    private int port;

    private String user;

    private String password;

    private String delimiter;

    private boolean hasDelimiter;

    private long fileLastModified;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "config")
    private List<Campo> campos;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "config")
    private Set<Header> headers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public IntegrationType getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(IntegrationType integrationType) {
        this.integrationType = integrationType;
    }

    public void setIntegrationType(String integrationType) {
        this.integrationType = IntegrationType.valueOf(integrationType);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isHasDelimiter() {
        return hasDelimiter;
    }

    public void setHasDelimiter(boolean hasDelimiter) {
        this.hasDelimiter = hasDelimiter;
    }

    public List<Campo> getCampos() {
        return campos;
    }

    public void setCampos(List<Campo> campos) {
        this.campos = campos;
    }

    public long getFileLastModified() {
        return fileLastModified;
    }

    public void setFileLastModified(long fileLastModified) {
        this.fileLastModified = fileLastModified;
    }

    public Set<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<Header> headers) {
        this.headers = headers;
    }
}

enum IntegrationType {
    FILE,
    WEB_SERVICE,
    DATABASE
}