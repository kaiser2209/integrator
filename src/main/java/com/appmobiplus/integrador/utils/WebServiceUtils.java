package com.appmobiplus.integrador.utils;

import java.util.regex.Pattern;

public class WebServiceUtils {

    public static String getAbsolutUrl(String url) {
        return url.split(Pattern.quote("?"))[0];
    }

    public static String[] getParameters(String url) {
        return url.split(Pattern.quote("?"))[1].split(Pattern.quote("&"));
    }
}
