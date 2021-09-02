package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigUtils {
    private static final String configFolder = "config/";
    private static final String configFileName = "integrador.config";
    private static Config config;
    public static String lastErrorMessage;
    private static ConfigAuth configAuth;
    private static ConfigCadastroProdutos configCadastroProdutos;

    public static boolean saveConfig(Config config) {
        try {
            checkAndCreateDirectory(configFolder);
            FileOutputStream fileOutputStream = new FileOutputStream(configFolder + configFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(config);
            objectOutputStream.close();
            ConfigUtils.config = config;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            lastErrorMessage = e.getMessage();
        }

        return false;
    }

    public static boolean saveConfig() {
        return saveConfig(config);
    }

    public static Config loadConfig() {
        if (!fileExists(configFolder + configFileName)) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(configFolder + configFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Config config = (Config) objectInputStream.readObject();
            objectInputStream.close();
            ConfigUtils.config = config;
            return config;

        } catch (Exception e) {
            lastErrorMessage = e.getMessage();
            LogUtils.saveLog("Falha ao recuperar a configuração. - " + e.getMessage());
        }

        return null;
    }

    public static boolean hasConfig() {
        return config != null;
    }

    public static Config getCurrentConfig() {
        if (!hasConfig()) {
            return loadConfig();
        }

        return config;
    }

    public static void checkAndCreateDirectory(String path) {
        File directory = new File(path);
         if(!directory.exists()) {
             directory.mkdirs();
         }

    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String getIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void setConfigAuth(ConfigAuth configAuth) {
        ConfigUtils.configAuth = configAuth;
    }

    public static ConfigAuth getConfigAuth() {
        return configAuth;
    }

    public static void setConfigCadastroProdutos(ConfigCadastroProdutos configCadastroProdutos) {
        ConfigUtils.configCadastroProdutos = configCadastroProdutos;
    }

    public static ConfigCadastroProdutos getConfigCadastroProdutos() {
        return configCadastroProdutos;
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        ConfigUtils.config = config;
    }

    public static ResponseEntity<String> getResponseProduto(Set<Header> authHeaders, ConfigBuscaProduto configBusca) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = getHttpHeaders(authHeaders);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBusca = mapper.writeValueAsString(configBusca.getSearchParameters());

        HttpEntity<String> request = new HttpEntity<>(jsonBusca, headers);

        return restTemplate.exchange(configBusca.getPath(), configBusca.getMethod(), request, String.class);
    }

    public static boolean verifyAndRenewToken(Set<Header> authHeaders, ConfigBuscaProduto configBusca, ConfigAuth configAuth) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = getHttpHeaders(authHeaders);

        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(configBusca.getPath(), configBusca.getMethod(), request, String.class);
        } catch (HttpClientErrorException e) {
            if(e.getRawStatusCode() == 401) {
                System.out.println(e.getStatusCode());
                renewToken(configAuth, authHeaders);
            }
            else
                return true;

        }

        return false;
    }

    public static ResponseEntity<String> getResponse(Set<Header> authHeaders, ConfigBuscaProduto configBusca, ConfigAuth configAuth) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = getHttpHeaders(authHeaders);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBusca = mapper.writeValueAsString(configBusca.getSearchParameters());

        HttpEntity<String> request = new HttpEntity<>(jsonBusca, headers);

        return restTemplate.exchange(configBusca.getPath(), configBusca.getMethod(), request, String.class);
    }

    public static boolean renewToken(ConfigAuth configAuth, Set<Header> authHeaders) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();

        for(String key : configAuth.getBodyParameters().keySet()) {
            parameters.add(key, configAuth.getBodyParameters().get(key));
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = restTemplate.exchange(configAuth.getPath(), configAuth.getMethod(), request, String.class);

        JsonNode authJson = JsonUtils.getJsonObject(response.getBody());
        String authorization = "";
        for(String field : configAuth.getFieldsUsedInAuth()) {
            authorization += authJson.get(field).textValue() + " ";
        }

        authorization = authorization.trim();

        System.out.println(authHeaders);

        for(Header h : authHeaders) {
            if(h.getKey().equals(configAuth.getAuthField())) {
                h.setValue(authorization);
            }
        }

        System.out.println(authHeaders);

        ConfigUtils.saveConfig();

        return true;
    }

    public static HttpHeaders getHttpHeaders(Set<Header> headers) {
        HttpHeaders h = new HttpHeaders();
        for(Header header : headers) {
            h.add(header.getKey(), header.getValue());
        }

        return h;
    }

    public static Map<String, String> getMapParameters(String[] key, String[] value) {
        Map<String, String> parameters = new HashMap<>();
        for(int i = 0; i < key.length; i++) {
            parameters.put(key[i], value[i]);
        }

        return parameters;
    }
}
