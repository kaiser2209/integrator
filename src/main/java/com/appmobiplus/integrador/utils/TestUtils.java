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
        return "[ {\n" +
                "  \"NumeroEmpresa\" : 26,\n" +
                "  \"DescCompleta\" : \"ABC DRIVER QUADRO EXPOSITOR HOT WHELLS\",\n" +
                "  \"NumeroSegmento\" : 1,\n" +
                "  \"PrecoVenda\" : 79.9,\n" +
                "  \"Embalagem\" : 1.0,\n" +
                "  \"PrecoVendaCaixa\" : 79.9,\n" +
                "  \"QuantidadeCaixa\" : 1.0,\n" +
                "  \"DataInicioPromocao\" : \"2021-08-04T00:00:00\",\n" +
                "  \"DataFimPromocao\" : \"2021-10-03T00:00:00\",\n" +
                "  \"PrecoPromocao\" : 29.9,\n" +
                "  \"PromocaoAPartir\" : [ ],\n" +
                "  \"DataAtualizacao\" : null,\n" +
                "  \"StatusVenda\" : \"A\",\n" +
                "  \"ClassificacaoComercial\" : \"A\",\n" +
                "  \"CodigoAcessoPrincipal\" : \"7898994546016\",\n" +
                "  \"IdProduto\" : 1351,\n" +
                "  \"IdFamilia\" : 10955,\n" +
                "  \"IdProdutoBase\" : null,\n" +
                "  \"DataUltimaAtualizacao\" : \"2021-08-04T02:02:50\"\n" +
                "} ]";

    }
}
