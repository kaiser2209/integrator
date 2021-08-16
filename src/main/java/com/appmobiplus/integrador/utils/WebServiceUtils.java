package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Produto;
import com.appmobiplus.integrador.configuration.ProdutoBuilder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class WebServiceUtils {

    public static String getAbsolutUrl(String url) {
        return url.split(Pattern.quote("?"))[0];
    }

    /*
    public static String[] getParameters(String url) {
        return url.split(Pattern.quote("?"))[1].split(Pattern.quote("&"));
    }

     */

    public static Map<String, String> getParameters(String url) {
        String[] parameters = url.split(Pattern.quote("?"))[1].split(Pattern.quote("&"));
        Map<String, String> returnParameters = new HashMap<>();
        for(String p : parameters) {
            String[] parameter = p.split(Pattern.quote("="));
            returnParameters.put(parameter[0].toLowerCase(Locale.ROOT), parameter[1]);
        }

        return returnParameters;
    }

    public static String getWebServiceURL(String host, String[] parametersKey, String[] parametersValue) {
        String parameters = "";
        for(int i = 0; i < parametersKey.length; i++) {
            if (i == 0) {
                parameters += "?";
            } else {
                parameters += "&";
            }

            parameters += parametersKey[i] + "=" + parametersValue[i];
        }

        return host + parameters;
    }

    public static String getWebServiceURL(String host, Map<String, String> parameters) {
        String params = "?";
        int i = 1;
        for(String key : parameters.keySet()) {
            params += key + "=" + parameters.get(key);
            if(i < parameters.size()) {
                params += "&";
                i++;
            }
        }

        return host + params;
    }

    public static Produto getProdutoFromModel(com.appmobiplus.integrador.models.Produto produto) {
        Produto p = ProdutoBuilder.get()
                .setEan(produto.getEan())
                .setDescricao(produto.getDescricao())
                .setPreco_de(produto.getPreco_de())
                .setPreco_por(produto.getPreco_por())
                .setLink_image(produto.getLink_image())
                .build();

        return p;
    }
}
