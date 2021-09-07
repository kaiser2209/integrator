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

    public static String getJsonProductTest() {
        return "[\r\n" +
                " {\r\n" +
                "  \"NumeroEmpresa\":26,\r\n" +
                "  \"DescCompleta\":\"ABC DRIVER QUADRO EXPOSITOR HOT WHELLS\", \r\n" +
                "  \"NumeroSegmento\":1, \r\n" +
                "  \"PrecoVenda\":79.9, \r\n" +
                "  \"Embalagem\":1.0, \r\n" +
                "  \"PrecoVendaCaixa\":179.0, \r\n" +
                "  \"QuantidadeCaixa\":1.0, \r\n" +
                "  \"DataInicioPromocao\":\"2021-08-04T00:00:00\", \r\n" +
                "  \"DataFimPromocao\":\"2021-10-03T00:00:00\", \r\n" +
                "  \"PrecoPromocao\":29.9, \r\n" +
                " }\r\n" +
                "]";

    }
}
