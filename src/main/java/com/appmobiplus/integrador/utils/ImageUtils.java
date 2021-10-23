package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Produto;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
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
            LogUtils.saveLog("Imagem " + fileExtension + " não encontrada no servidor de imagens.");
        }

        return false;
    }

    public static boolean downloadImageNews(String imageUrl, String saveTo, String filename, String extension) {


        try {
            URL url = new URL(imageUrl);

            ConfigUtils.checkAndCreateDirectory(saveTo);

            InputStream in = new BufferedInputStream(url.openStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;

            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }

            out.close();
            in.close();
            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream(saveTo + filename + "." + extension);
            fos.write(response);
            fos.close();

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean downloadImageWithoutExtension(String from, String to, String fileName) {
        BufferedImage image = null;
        ConfigUtils.checkAndCreateDirectory(to);

        try {
            URL url = new URL(from + fileName);
            image = ImageIO.read(url);
            ImageIO.write(image, "jpg", new File(to + fileName + ".jpg"));
            return true;
        } catch (IOException e) {
            LogUtils.saveLog("Imagem não encontrada no servidor de imagens.");
        }

        return false;
    }

    public static boolean verifyAndDownloadImage(String from, String to, String filename, String fileExtension) {
        File file = new File(to + filename + "." + fileExtension);
        if (!file.exists()) {
            return  downloadImage(from, to, filename, fileExtension);
        }

        return true;
    }

    public static boolean verifyAndDownloadImageNews(String url, String saveTo, String filename, String extension) {
        File file = new File(saveTo + filename + "." + extension);
        if (!file.exists()) {
            return downloadImageNews(url, saveTo, filename, extension);
        }

        return true;
    }

    public static boolean verifyAndDownloadImageNews(String url, String saveTo, String filename) {
        File file = new File(saveTo + filename + "." + "jpg");
        if (!file.exists()) {
            return downloadImageNews(url, saveTo, filename, "jpg");
        }

        return true;
    }

    public static boolean verifyAndDownload(String url, String saveTo, String filename) {
        File file = new File(saveTo + filename);
        if (!file.exists()) {
            return download(url, saveTo, filename);
        }

        return true;
    }

    public static boolean download(String fileUrl, String saveTo, String filename) {
        try {
            URL url = new URL(fileUrl);

            ConfigUtils.checkAndCreateDirectory(saveTo);

            InputStream in = new BufferedInputStream(url.openStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;

            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }

            out.close();
            in.close();
            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream(saveTo + filename);
            fos.write(response);
            fos.close();

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyAndDownloadImage(String from, String to, String filename) {
        File file = new File(to + filename + ".jpg");
        if (!file.exists()) {
            return  downloadImageWithoutExtension(from, to, filename);
        }

        return true;
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

    public static void updateLinkImage(Produto produto) {
        if(produto.getLink_image() == null || produto.getLink_image().isEmpty()) {
            if(!hasImageDownloaded(produto.getEan(), "png")) {
                if (downloadImage(getImageServerPath(), getLocalPath(), produto.getEan(), "png")) {
                    produto.setLink_image(getImagePath(produto.getEan(), "png"));
                }
            } else {
                produto.setLink_image(getImagePath(produto.getEan(), "png"));
            }
        }
    }
}
