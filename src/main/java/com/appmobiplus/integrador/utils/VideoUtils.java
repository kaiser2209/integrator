package com.appmobiplus.integrador.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VideoUtils {

    public static boolean downloadVideo(String from, String to, String fileName, String fileExtension) {
        ConfigUtils.checkAndCreateDirectory(to);

        try {
            URL url = new URL(from + fileName + "." + fileExtension);

            try (InputStream in = url.openStream();
                 ReadableByteChannel rbc = Channels.newChannel(in)) {
                File file = new File(to + fileName + "." + fileExtension);
                //FileOutputStream fos = new FileOutputStream(file);
                //fos.getChannel().transferFrom(rbc, 0 ,);
                Files.copy(in, Paths.get(file.getAbsolutePath()));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean verifyAndDownloadVideo(String from, String to, String filename, String fileExtension) {
        File file = new File(to + filename + "." + fileExtension);
        if (!file.exists()) {
            return  downloadVideo(from, to, filename, fileExtension);
        }

        return true;
    }
}
