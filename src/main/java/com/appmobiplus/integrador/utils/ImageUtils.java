package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    private static String localPath = "download/images/";
    private static String imageServerPath = "https://storage-api.appmobiplus.com/app/produtos/";

    public static boolean downloadImage(String from, String to, String fileName, String fileExtension) {
        BufferedImage image = null;
        ConfigUtils.checkAndCreateDirectory(localPath);

        try {
            URL url = new URL(from + fileName + "." + fileExtension);
            image = ImageIO.read(url);
            ImageIO.write(image, fileExtension, new File(localPath + fileName + "." + fileExtension));
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static String getLocalImagePath(String filename, String fileExtension) {
        return localPath + filename + "." + fileExtension;
    }

    public static String getLocalPath() {
        return localPath;
    }

    public static String getImageServerPath() {
        return imageServerPath;
    }
}
