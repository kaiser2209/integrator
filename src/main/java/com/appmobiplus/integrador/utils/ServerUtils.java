package com.appmobiplus.integrador.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class ServerUtils {

    private static int port;

    public static void setPort(int port) {
        ServerUtils.port = port;
    }

    public static int getPort() {
        return port;
    }
}
