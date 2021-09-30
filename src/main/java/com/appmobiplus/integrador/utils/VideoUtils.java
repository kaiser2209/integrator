package com.appmobiplus.integrador.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VideoUtils {

    public static boolean downloadImage(String from, String to, String fileName, String fileExtension) {
        BufferedImage image = null;
        ConfigUtils.checkAndCreateDirectory(to);

        try {
            URL url = new URL(from + fileName + "." + fileExtension);
            image = ImageIO.read(url);
            ImageIO.write(image, fileExtension, new File(to + fileName + "." + fileExtension));
            return true;
        } catch (IOException e) {
            LogUtils.saveLog("Imagem " + fileExtension + " não encontrada no servidor de imagens.");
        }

        return false;
    }
}
