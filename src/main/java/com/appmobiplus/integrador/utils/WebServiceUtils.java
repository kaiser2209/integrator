package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Config;
import com.appmobiplus.integrador.configuration.Produto;
import com.appmobiplus.integrador.configuration.ProdutoBuilder;
import com.appmobiplus.integrador.firebase.DocumentReferenceAttributes;
import com.appmobiplus.integrador.firebase.FirestoreConfig;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import org.apache.juli.logging.Log;
import org.springframework.ui.ModelMap;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

public class WebServiceUtils {
    private static final String mediaPath = "https://storage-api.appmobiplus.com/app/";

    public static String getAbsolutUrl(String url) {
        return url.split(Pattern.quote("?"))[0];
    }

    public static Map<String, String> getParameters(String url) {
        Map<String, String> returnParameters = new HashMap<>();
        if (url.contains("?")) {
            String[] parameters = url.split(Pattern.quote("?"))[1].split(Pattern.quote("&"));

            for (String p : parameters) {
                String[] parameter = p.split(Pattern.quote("="));
                returnParameters.put(parameter[0].toLowerCase(Locale.ROOT), parameter[1]);
            }
        }

        return returnParameters;
    }

    public static String getWebServiceURL(String host, String[] parametersKey, String[] parametersValue) {
        String parameters = "";
        for(int i = 0; i < parametersKey.length; i++) {
            if (i == 0) {
                parameters += "?";
            } else {
                parameters += "&";
            }

            parameters += parametersKey[i] + "=" + parametersValue[i];
        }

        return host + parameters;
    }

    public static String getWebServiceURL(String host, Map<String, String> parameters) {
        String params = "?";
        int i = 1;
        for(String key : parameters.keySet()) {
            params += key + "=" + parameters.get(key);
            if(i < parameters.size()) {
                params += "&";
                i++;
            }
        }

        return host + params;
    }

    public static Produto getProdutoFromModel(com.appmobiplus.integrador.models.Produto produto) {
        Produto p = ProdutoBuilder.get()
                .setEan(produto.getEan())
                .setDescricao(produto.getDescricao())
                .setPreco_de(produto.getPreco_de())
                .setPreco_por(produto.getPreco_por())
                .setLink_image(produto.getLink_image())
                .build();

        return p;
    }

    public static String showDialogError(ModelMap map, Exception e) {
        LogUtils.saveLog(e.getMessage());
        LogUtils.saveLog(Arrays.toString(e.getStackTrace()));
        map.addAttribute("title", "Erro");
        map.addAttribute("errorMessage", e.getMessage());
        return "dataFragments :: #dialog-error";
    }

    public static DocumentReference getDocumentReference(DocumentReferenceAttributes document) {
        DocumentReference db = FirestoreConfig.getFirestoreDB().collection("DB")
                .document(document.getIdCompany())
                .collection("Grupos")
                .document(document.getIdGroup());

        System.out.println("Configurando");
        System.out.println("Company: " + document.getIdCompany());
        System.out.println("Group: " + document.getIdGroup());

        db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
                System.out.println("Antes da verificação de erro");
                if (e != null) {
                    return;
                }

                System.out.println("Depois da verificação de erro");
                //System.out.println(documentSnapshot.getData());
                document.setData((Map<String, Object>) documentSnapshot.getData());
                //System.out.println(Arrays.toString(data.toArray()));
                List<Map<String, Object>> data = (List<Map<String, Object>>) document.getData().get("data");
                downloadAndChangeImagesToLocalPath(data);
                downloadAndChangeVideosToLocalPath(data);

            }
        });

        return db;
    }

    public static void downloadAndChangeImagesToLocalPath(List<Map<String, Object>> data) {
        for(Map<String, Object> d : data) {
            if(d.get("type").equals("image")) {
                String name = (String) d.get("name");
                String filename = name.substring(0, name.lastIndexOf("."));
                String extension = name.substring(name.lastIndexOf(".") + 1);
                String id = (String) d.get("id");
                String imagePath = createImagePath(id);
                String thumbImagePath = imagePath + "/thumbs";
                String sourceImagePath = (mediaPath + imagePath).replaceAll("image/", "");
                String sourceThumbImagePath = (mediaPath + thumbImagePath).replaceAll("image/", "");
                ImageUtils.verifyAndDownloadImage(sourceImagePath + "/", imagePath + "/", filename, extension);
                ImageUtils.verifyAndDownloadImage(sourceThumbImagePath + "/", thumbImagePath + "/", filename, extension);
                d.put("link", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + imagePath + "/" + filename + "." + extension);
                d.put("thumbUrl", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + thumbImagePath + "/" + filename + "." + extension);
            }
        }
    }

    public static void downloadAndChangeVideosToLocalPath(List<Map<String, Object>> data) {
        for(Map<String, Object> d : data) {
            if(d.get("type").equals("video")) {
                String name = (String) d.get("name");
                String id = (String) d.get("id");
                String filename = name.substring(0, name.lastIndexOf("."));
                String extension = name.substring(name.lastIndexOf(".") + 1);
                String videoPath = createVideoPath(id);
                String sourceVideoPath = (mediaPath + videoPath).replaceAll("video/", "");
                VideoUtils.verifyAndDownloadVideo(sourceVideoPath + "/", videoPath + "/", filename, extension);
                d.put("link", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + videoPath + "/" + filename + "." + extension);
            }
        }
    }

    public static String createImagePath(String path) {
        String imagePath = "midias/image/" + path.substring(0, path.lastIndexOf("/"));
        ConfigUtils.checkAndCreateDirectory(imagePath);
        System.out.println(imagePath);

        return imagePath;
    }

    public static String createVideoPath(String path) {
        String videoPath = "midias/video/" + path.substring(0, path.lastIndexOf("/"));
        ConfigUtils.checkAndCreateDirectory(videoPath);
        System.out.println(videoPath);

        return videoPath;
    }

}
