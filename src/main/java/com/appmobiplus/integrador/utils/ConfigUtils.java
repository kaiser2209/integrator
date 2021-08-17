package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Config;
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
            e.printStackTrace();
            lastErrorMessage = e.getMessage();
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
}
