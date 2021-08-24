package com.appmobiplus.integrador.utils;

public class TestUtils {
    public static String[] getAuthTestKeys() {
        String[] keys = {"password", "username", "grant_type", "client_secret", "client_id"};
        return keys;
    }

    public static String[] getAuthTestValues() {
        String[] values = {"1010", "buscapreco", "password", "poder7547", "cisspoder-oauth"};
        return values;
    }

    public static String getAuthUrl() {
        return "http://189.38.1.130:4664/cisspoder-auth/oauth/token";
    }
}
