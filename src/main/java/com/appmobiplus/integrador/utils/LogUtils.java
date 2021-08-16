package com.appmobiplus.integrador.utils;

import jdk.dynalink.StandardOperation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LogUtils {
    private static String logPath = "log/";
    private static String logFile = "log.txt";

    public static void saveLog(String message) {
        File path = new File(logPath);
        File file = new File(logPath + logFile);

        try {

        if(!path.exists()) {
            path.mkdirs();
        }

        if(!file.exists()) {
            file.createNewFile();
        }

        String dateNow = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM));
        String line = dateNow + ": " + message + "\n";

            Files.write(file.toPath(), line.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
