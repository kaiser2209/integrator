package com.appmobiplus.integrador.utils;

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
