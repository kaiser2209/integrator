package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Config;
import com.appmobiplus.integrador.configuration.ConfigAuth;
import com.appmobiplus.integrador.configuration.ConfigCadastroProdutos;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

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
}
