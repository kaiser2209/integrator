package com.appmobiplus.integrador.utils;

import com.appmobiplus.integrador.configuration.Field;
import com.appmobiplus.integrador.models.Config;
import com.appmobiplus.integrador.models.Produto;
import com.appmobiplus.integrador.repositories.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public class FileUtils {
    public static String[] lines(File file) {

        return null;
    }

    public static List<String> lines(String path, int totalLines) throws IOException {
        List<String> lines = new ArrayList<>();
        int actualLine = 0;
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null && actualLine < totalLines) {
            lines.add(line);
            //System.out.println(line.length());
            actualLine++;
        }

        return lines;
    }

    public static List<Produto> getProdutos(Config config) throws IOException {
        String[] campos = new String[config.getCampos().size()];
        int[] posBegin = new int[config.getCampos().size()];
        int[] posEnd = new int[config.getCampos().size()];
        String fieldPrice = "";
        int decimalPoint = 0;
        for(int i = 0; i < config.getCampos().size(); i++) {
            campos[i] = config.getCampos().get(i).getNewName();
            posBegin[i] = config.getCampos().get(i).getInitialPos();
            posEnd[i] = config.getCampos().get(i).getFinalPos();
            if (config.getCampos().get(i).isPriceField()) {
                fieldPrice = campos[i];
                decimalPoint = config.getCampos().get(i).getDecimalPoint();
            }
        }
        System.out.println(config.getCampos());

        return getProdutos(config.getPath(), config.isHasDelimiter(), config.getDelimiter(), campos,
                posBegin, posEnd, fieldPrice, decimalPoint);
    }

    public static List<Produto> getProdutos(com.appmobiplus.integrador.configuration.Config config) throws IOException {
        String[] campos = new String[config.getFields().size()];
        int[] posBegin = new int[config.getFields().size()];
        int[] posEnd = new int[config.getFields().size()];
        String fieldPrice = "";
        int decimalPoint = 0;
        int count = 0;
        for(Field f : config.getFields()) {
            campos[count] = f.getNewName();
            posBegin[count] = f.getPosBegin();
            posEnd[count] = f.getPosEnd();
            if(f.isCurrencyField()) {
                fieldPrice = campos[count];
                decimalPoint = f.getDecimalPoint();
            }
            count++;
        }
        /*
        for(int i = 0; i < config.getFields().size(); i++) {
            campos[i] = config.getFields().get(i).getNewName();
            posBegin[i] = config.getCampos().get(i).getInitialPos();
            posEnd[i] = config.getCampos().get(i).getFinalPos();
            if (config.getCampos().get(i).isPriceField()) {
                fieldPrice = campos[i];
                decimalPoint = config.getCampos().get(i).getDecimalPoint();
            }
        }
        System.out.println(config.getCampos());

         */

        return getProdutos(config.getPath(), config.isHasDelimiter(), config.getDelimiter(), campos,
                posBegin, posEnd, fieldPrice, decimalPoint);
    }

    public static List<Produto> getProdutos(String path, boolean hasDelimiter, String delimiter, String[] campos,
                                            int[] posBegin, int[] posEnd, String fieldPrice,
                                            int decimalPoint) throws IOException {
        List<Produto> produtos = new ArrayList<>();

        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while((line = br.readLine()) != null) {
            Map<String, String> mapa = new HashMap<String, String>();
            String[] dados;

            if(hasDelimiter) {
                dados = dados(line, delimiter);
                //System.out.println("Delimitador: " + hasDelimiter);
            } else {
                dados = dados(line, posBegin, posEnd);
            }

            boolean hasNull = hasNull(dados);

            if (!(dados.length > campos.length || hasNull)) {
                for (int i = 0; i < campos.length; i++) {
                    mapa.put(campos[i], dados[i]);
                }

                //System.out.println(mapa);

                Produto p = new Produto();
                p.setDescricao(mapa.get("descricao"));
                p.setPreco_de(getValue(mapa.get(fieldPrice), decimalPoint));
                p.setEan(mapa.get("ean"));

                //if (ImageUtils.downloadImage(ImageUtils.getImageServerPath(), ImageUtils.getLocalPath(), p.getEan(), "png")) {
                //    p.setLink_image(ImageUtils.getLocalImagePath(p.getEan(), "png"));
                //}

                produtos.add(p);
                //System.out.println(p);
            }
        }

        return produtos;
    }

    public static String[] dados(String linha, String delimiter) {
        String[] dados = linha.split(Pattern.quote(delimiter));
        return dados;
    }

    public static String[] dados(String linha, int[] posBegin, int[] posEnd) {
        String[] dados = new String[posBegin.length];
        for(int i = 0; i < posBegin.length; i++) {
            dados[i] = linha.substring(posBegin[i], posEnd[i]);
        }

        return dados;
    }

    public static float getValue(String valor, int decimalPoint) {
        float f = 0f;
        try {
            NumberFormat format = NumberFormat.getInstance();
            Number number = format.parse(valor);
            f = number.floatValue();
        } catch (ParseException e) {
            f = Float.parseFloat(valor);
        } finally {
            float divisor = (float) Math.pow(10, decimalPoint);
            if(!valor.contains(".") && !valor.contains(",")) {
                f = f / divisor;
            }

            return f;
        }

    }

    public static boolean hasNull(String[] array) {
        for(String a : array) {
            if(a.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static long getFileLastModificationTime(String path) {
        File file = new File(path);

        return file.lastModified();
    }

    public static String getFormattedJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        String returnJson = mapper.writeValueAsString(jsonNode);

        return returnJson;
    }
}
