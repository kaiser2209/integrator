package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Produto;
import com.appmobiplus.integrador.configuration.ProdutoBuilder;
import com.appmobiplus.integrador.firebase.DocumentReferenceAttributes;
import com.appmobiplus.integrador.firebase.FirestoreConfig;
import com.appmobiplus.integrador.firebase.databases.DayWeather;
import com.appmobiplus.integrador.firebase.databases.DayWeatherCurrent;
import com.appmobiplus.integrador.firebase.databases.Grupo;
import com.appmobiplus.integrador.firebase.databases.WeatherBase;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import org.apache.commons.io.FilenameUtils;
import org.springframework.ui.ModelMap;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class WebServiceUtils {
    private static final String mediaPath = "https://storage-api.appmobiplus.com/app/";
    private static Map<String, Object> weatherData = new HashMap<>();
    private static Map<String, Object> newsData = new HashMap<>();

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
                //document.setData((Map<String, Object>) documentSnapshot.getData());
                document.setGrupo(documentSnapshot.toObject(Grupo.class));
                //List<Map<String, Object>> data = (List<Map<String, Object>>) document.getData().get("data");
                List<Map<String, Object>> data = document.getGrupo().getData();

                for(Map<String, Object> d : data) {
                    switch ((String) d.get("type")) {
                        case "image":
                            downloadAndChangeImagesToLocalPath(d);
                            break;
                        case "video":
                            downloadAndChangeVideosToLocalPath(d);
                            break;
                        case "weather":
                            setWeatherData(d);
                            break;
                        case "news":
                            setNewsData(d);
                            break;
                        case "table":
                            setTableData(d);
                            break;
                        default:
                        System.out.println("Nenhuma opção");
                    }
                }

            }
        });

        return db;
    }

    public static void setTableData(final Map<String, Object> table) {
        String backgroundUrl = (String) table.get("headerBackground");
        if (backgroundUrl != null) {
            String to = "midias/table/";
            String file = backgroundUrl.substring(backgroundUrl.lastIndexOf("/") + 1);
            String extension = file.substring(file.lastIndexOf(".") + 1);
            String filename = file.substring(0, file.lastIndexOf("."));
            if(ImageUtils.verifyAndDownload(backgroundUrl, to, file)) {
                table.put("headerBackground", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" +  to + file);
            }
            System.out.println(file);
            System.out.println(filename);
            System.out.println(extension);
            System.out.println(backgroundUrl);
        }
    }

    public static void setWeatherData(final Map<String, Object> weather) {
        DocumentReference db = FirestoreConfig.getFirestoreDB().collection("DBgeral")
                .document("Climas")
                .collection("base")
                .document((String) weather.get("id"));

        if (!weatherData.containsKey(weather.get("id"))) {
            db.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
                    if (e != null) {
                        System.out.println("Erro: " + e.getMessage());
                        return;
                    }

                    Map<String, Object> data = new HashMap<>();
                    Map<String, Object> current = (Map<String, Object>) ((Map<String, Object>) documentSnapshot.get("nextDays")).get("current");
                    List<Map<String, Object>> daily = (ArrayList<Map<String, Object>>) ((Map<String, Object>) documentSnapshot.get("nextDays")).get("daily");

                    WeatherBase base = documentSnapshot.toObject(WeatherBase.class);
                    DayWeatherCurrent currentDayWeather = base.getNextDays().getCurrent();
                    List<DayWeather> dailyWeather = base.getNextDays().getDaily();

                    System.out.println("Last Updated: " + base.getLastUpdated());
                    System.out.println("Clouds All: " + base.getData().getClouds().getAll());

                    System.out.println("Classe do humidity: " + current.get("humidity").getClass());


                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Calendar calendar = Calendar.getInstance();
                    //calendar.setTime(((Timestamp) documentSnapshot.get("lastUpdated")).toDate());
                    calendar.setTime(base.getLastUpdated().toDate());

                    /*data.put("temperature", current.get("temp"));
                    data.put("moonPhase", daily.get(0).get("moon_phase"));
                    data.put("windSpeed", current.get("wind_speed"));
                    data.put("date", sdf.format(calendar.getTime()));
                    data.put("day", getCurrentDay(calendar.get(Calendar.DAY_OF_WEEK)));
                    data.put("uvIndex", current.get("uvi"));
                    data.put("cityCode", documentSnapshot.get("cidade"));
                    data.put("cityName", ((Map<String, Object>) documentSnapshot.get("data")).get("name"));
                    data.put("icon", ((ArrayList<Map<String, Object>>) current.get("weather")).get(0).get("icon"));*/
                    data.put("temperature", currentDayWeather.getTemp());
                    data.put("moonPhase", dailyWeather.get(0).getMoon_phase());
                    data.put("windSpeed", currentDayWeather.getWind_speed());
                    data.put("date", sdf.format(calendar.getTime()));
                    data.put("day", getCurrentDay(calendar.get(Calendar.DAY_OF_WEEK)));
                    data.put("uvIndex", currentDayWeather.getUvi());
                    data.put("cityCode", base.getCidade());
                    data.put("cityName", base.getData().getName());
                    data.put("icon", currentDayWeather.getWeather().get(0).getIcon());

                    List<Map<String, Object>> nextDays = new ArrayList<>();

                    for (int i = 1; i <= 7; i++) {
                        Map<String, Object> day = new HashMap<>();
                        Calendar nextCalendar = Calendar.getInstance();
                        nextCalendar.add(Calendar.DAY_OF_MONTH, i);
                        /*day.put("temperatureMax", ((Map<String, Object>) daily.get(i).get("temp")).get("max"));
                        day.put("temperaturaMin", ((Map<String, Object>) daily.get(i).get("temp")).get("min"));
                        day.put("dayWeek", getCurrentDay(nextCalendar.get(Calendar.DAY_OF_WEEK)));
                        day.put("date", sdf.format(nextCalendar.getTime()));
                        day.put("iconAsset", getIcon(((ArrayList<Map<String, Object>>) (daily.get(i).get("weather"))).get(0).get("icon").toString(),
                                (Long) ((ArrayList<Map<String, Object>>) (daily.get(i).get("weather"))).get(0).get("id")));*/
                        day.put("temperatureMax", dailyWeather.get(i).getTemp().getMax());
                        day.put("tempratureMin", dailyWeather.get(i).getTemp().getMin());
                        day.put("dayWeek", getCurrentDay(nextCalendar.get(Calendar.DAY_OF_WEEK)));
                        day.put("date", sdf.format(nextCalendar.getTime()));
                        day.put("iconAsset", getIcon(dailyWeather.get(i).getWeather().get(0).getIcon(),
                                dailyWeather.get(i).getWeather().get(0).getId()));
                        nextDays.add(day);
                    }

                    data.put("nextDays", nextDays);

                    System.out.println("Buscou weather data");

                    System.out.println(weather.get("id"));

                    weatherData.put((String) weather.get("id"), data);

                    updateWeatherData(weather);
                }
            });
        } else {
            updateWeatherData(weather);
        }

    }

    private static String getIcon(String weatherIconCode, Long weatherCode) {
        //céu limpo
        if (weatherCode == 800) {
            if (weatherIconCode.equals("01d")) {
                return "clear-day.png"; //céu limpo dia
            } else if (weatherIconCode.equals("01n")) {
                return "clear-night.png"; //céu limpo noite
            }
        } else if (weatherCode >= 500 && weatherCode < 600) { //chuva
            return "rain.png";
        } else if (weatherCode >= 600 && weatherCode < 603 && weatherCode >= 615 && weatherCode < 700) { //neve
            return "snow.png";
        } else if (weatherCode >= 611 && weatherCode <= 613) { //granizo
            return "sleet.png";
        } else if (weatherCode == 741) { //neblina
            return "fog.png";
        } else if (weatherCode == 801) { //
            if (weatherIconCode.equals("02d")) {
                return "partly-cloudy-day.png";
            } else if (weatherIconCode.equals("02n")) {
                return "partly-cloudy-night.png";
            }
        } else if (weatherCode >= 802 && weatherCode <= 804) {
            return "cloudy.png";
        }
        return "error";
    }


    public static void setNewsData(Map<String, Object> news) {
        CollectionReference ref = FirestoreConfig.getFirestoreDB().collection("DBgeral")
                .document("Noticias")
                .collection((String) news.get("category"));

        if(!newsData.containsKey(news.get("category"))) {
            ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                    if (e != null) {
                        return;
                    }

                    Map<String, Object> newsReceived = new HashMap<>();

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (doc.getType()) {
                            case ADDED:
                            case MODIFIED:
                                newsReceived.put(doc.getDocument().getId(), doc.getDocument().getData());
                                break;
                            case REMOVED:
                                newsReceived.remove(doc.getDocument().getId());
                                break;
                            default:
                                break;
                        }
                    }

                    List<Map<String, Object>> allNews = new ArrayList<>();

                    for (String key : newsReceived.keySet()) {
                        allNews.add((Map<String, Object>) newsReceived.get(key));
                    }

                    downloadNewsImages(allNews);

                    System.out.println(news);

                    newsData.put((String) news.get("category"), allNews);

                    updateNewsData(news);
                }
            });

        } else {
            updateNewsData(news);
        }
    }

    public static void updateWeatherData(Map<String, Object> weather) {
        Map<String, Object> data = (Map<String, Object>) weatherData.get((String) weather.get("id"));

        for(String d : data.keySet()) {
            weather.put(d, data.get(d));
        }
    }

    public static void updateNewsData(Map<String, Object> news) {
        System.out.println(news);
        try {
            long limit = (long) news.get("limit");
            List<Map<String, Object>> data = (List<Map<String, Object>>) newsData.get(news.get("category"));

            Collections.sort(data, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    /*Calendar c1 = Calendar.getInstance();
                    c1.setTime(((Timestamp) o1.get("datePublished")).toDate());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(((Timestamp) o2.get("datePublished")).toDate());*/

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
                    LocalDateTime d1 = LocalDateTime.parse((String) o1.get("datePublished"), formatter);
                    LocalDateTime d2 = LocalDateTime.parse((String) o2.get("datePublished"), formatter);
                    return d2.compareTo(d1);
                }
            });

            List<Map<String, Object>> filteredNews = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                filteredNews.add(data.get(i));
            }

            news.put("data", filteredNews);
        } catch (Exception e) {
            e.printStackTrace();

        }
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

    public static void downloadNewsImages(List<Map<String, Object>> allNews) {
        try {
            for (Map<String, Object> news : allNews) {
                String stringUrl = null;
                URL url = null;
                try {
                    stringUrl = URLDecoder.decode((String) news.get("image"), StandardCharsets.UTF_8.name());
                    url = new URL(stringUrl);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println(stringUrl);
                String file = FilenameUtils.getName(url.getPath());
                System.out.println("Nome do arquivo -> " + file);
                ImageUtils.verifyAndDownload(stringUrl, "midias/news/", file);


                news.put("image", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/midias/news/" + file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadAndChangeImagesToLocalPath(Map<String, Object> imageData) {
        String name = (String) imageData.get("name");
        String filename = name.substring(0, name.lastIndexOf("."));
        String extension = name.substring(name.lastIndexOf(".") + 1);
        String id = (String) imageData.get("id");
        String imagePath = createImagePath(id);
        String thumbImagePath = imagePath + "/thumbs";
        String sourceImagePath = (mediaPath + imagePath).replaceAll("image/", "");
        String sourceThumbImagePath = (mediaPath + thumbImagePath).replaceAll("image/", "");
        //ImageUtils.verifyAndDownloadImage(sourceImagePath + "/", imagePath + "/", filename, extension);
        //ImageUtils.verifyAndDownloadImage(sourceThumbImagePath + "/", thumbImagePath + "/", filename, extension);
        ImageUtils.verifyAndDownload((String) imageData.get("link"), imagePath + "/", name);
        ImageUtils.verifyAndDownload((String) imageData.get("thumbUrl"), thumbImagePath + "/", name);
        imageData.put("link", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + imagePath + "/" + filename + "." + extension);
        imageData.put("thumbUrl", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + thumbImagePath + "/" + filename + "." + extension);
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
                //ImageUtils.verifyAndDownloadImage(sourceImagePath + "/", imagePath + "/", filename, extension);
                //ImageUtils.verifyAndDownloadImage(sourceThumbImagePath + "/", thumbImagePath + "/", filename, extension);
                ImageUtils.verifyAndDownload((String) d.get("link"), imagePath + "/", name);
                ImageUtils.verifyAndDownload((String) d.get("thumbUrl"), imagePath + "/", name);

                d.put("link", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + imagePath + "/" + filename + "." + extension);
                d.put("thumbUrl", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + thumbImagePath + "/" + filename + "." + extension);
            }
        }
    }

    public static void downloadAndChangeVideosToLocalPath(Map<String, Object> videoData) {
        String name = (String) videoData.get("name");
        String id = (String) videoData.get("id");
        String filename = name.substring(0, name.lastIndexOf("."));
        String extension = name.substring(name.lastIndexOf(".") + 1);
        String videoPath = createVideoPath(id);
        String sourceVideoPath = (mediaPath + videoPath).replaceAll("video/", "");
        VideoUtils.verifyAndDownloadVideo(sourceVideoPath + "/", videoPath + "/", filename, extension);
        videoData.put("link", ConfigUtils.getIpAddress() + ":" + ServerUtils.getPort() + "/" + videoPath + "/" + filename + "." + extension);
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
