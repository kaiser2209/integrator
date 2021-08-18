package com.appmobiplus.integrador.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
public class ImageUtils {
    private static String localPath = "download/images/";
    private static String imageServerPath = "https://storage-api.appmobiplus.com/app/produtos/";

    public static boolean downloadImage(String from, String to, String fileName, String fileExtension) {
        BufferedImage image = null;
        ConfigUtils.checkAndCreateDirectory(to);

        try {
            URL url = new URL(from + fileName + "." + fileExtension);
            image = ImageIO.read(url);
            ImageIO.write(image, fileExtension, new File(to + fileName + "." + fileExtension));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String downloadImage(String originLink, String ean, String saveTo) {
        BufferedImage image = null;
        ConfigUtils.checkAndCreateDirectory(saveTo);

        try {
            URL url = new URL(originLink);
            String saveToWithFilename = saveTo + ean + "." + "png";
            File file = new File(saveToWithFilename);
            if(!file.exists()) {
                image = ImageIO.read(url);
                ImageIO.write(image, "png", new File(saveToWithFilename));
            }
            return ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/image/" + ean + "." + "png";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLocalImagePath(String filename, String fileExtension) {
        return localPath + filename + "." + fileExtension;
    }

    public static String getImagePath(String filename, String fileExtension) {
        return ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/image/" + filename + "." + fileExtension;
    }

    public static String getLocalPath() {
        return localPath;
    }

    public static String getImageServerPath() {
        return imageServerPath;
    }

    public static boolean hasImageDownloaded(String filename, String fileExtension) {
        File file = new File(localPath + filename + "." + fileExtension);
        return file.exists();
    }
}
