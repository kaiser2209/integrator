package com.appmobiplus.integrador.models;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;

@Entity
public class Header {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String key;
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "config_id", nullable = false)
    private Config config;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
