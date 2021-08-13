package com.appmobiplus.integrador.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    private static String localPath = "download/images/";
    private static String imageServerPath = "https://storage-api.appmobiplus.com/app/produtos/";

    public static boolean downloadImage(String from, String to, String filename, String fileExtension) {
        BufferedImage image = null;

        try {
            ConfigUtils.checkAndCreateDirectory(localPath);
            URL url = new URL(from + filename + "." + fileExtension);
            image = ImageIO.read(url);

            ImageIO.write(image, fileExtension, new File(to + filename + "." + fileExtension));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getLocalPath() {
        return localPath;
    }

    public static String getImageServerPath() {
        return imageServerPath;
    }
}
