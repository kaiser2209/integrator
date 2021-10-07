package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Config;
import com.appmobiplus.integrador.configuration.Produto;
import com.appmobiplus.integrador.configuration.ProdutoBuilder;
import com.appmobiplus.integrador.firebase.DocumentReferenceAttributes;
import com.appmobiplus.integrador.firebase.FirestoreConfig;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import org.apache.juli.logging.Log;
import org.springframework.ui.ModelMap;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
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
                setWeatherData(data);
            }
        });

        return db;
    }

    public static void setWeatherData(List<Map<String, Object>> data) {
        for(Map<String, Object> d : data) {
            if(d.get("type").equals("weather")) {
                DocumentReference db = FirestoreConfig.getFirestoreDB().collection("DBgeral")
                        .document("Climas")
                        .collection("base")
                        .document((String) d.get("id"));

                db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        Map<String, Object> current = (Map<String, Object>) ((Map<String, Object>) documentSnapshot.get("nextDays")).get("current");
                        List<Map<String, Object>> daily = (ArrayList<Map<String, Object>>) ((Map<String, Object>) documentSnapshot.get("nextDays")).get("daily");


                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(((Timestamp) documentSnapshot.get("lastUpdated")).toDate());

                        d.put("temperature", current.get("temp"));
                        d.put("moonPhase", daily.get(0).get("moon_phase"));
                        d.put("windSpeed", current.get("wind_speed"));
                        d.put("date", sdf.format(calendar.getTime()));
                        d.put("day", getCurrentDay(calendar.get(Calendar.DAY_OF_WEEK)));
                        d.put("uvIndex", current.get("uvi"));
                        d.put("cityCode", documentSnapshot.get("cidade"));
                        d.put("cityName", ((Map<String, Object>) documentSnapshot.get("data")).get("name"));
                        d.put("icon", ((ArrayList<Map<String, Object>>) current.get("weather")).get(0).get("icon"));
                    }
                });
            }
        }
    }

    public static String getCurrentDay(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return "Domingo";
            case Calendar.MONDAY:
                return "Segunda-Feira";
            case Calendar.TUESDAY:
                return "Terça-Feira";
            case Calendar.WEDNESDAY:
                return "Quarta-Feira";
            case Calendar.THURSDAY:
                return "Quinta-Feira";
            case Calendar.FRIDAY:
                return "Sexta-Feira";
            case Calendar.SATURDAY:
                return "Sábado";
            default:
                return "";
        }
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
