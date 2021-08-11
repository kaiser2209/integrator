package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.models.Campo;
import com.appmobiplus.integrador.models.Config;
import com.appmobiplus.integrador.repositories.CampoRepository;
import com.appmobiplus.integrador.repositories.ConfigRepository;
import com.appmobiplus.integrador.utils.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class WebServiceController {
    @Autowired
    ConfigRepository configRepository;

    @Autowired
    CampoRepository campoRepository;

    @PostMapping(value = "/config/ws/teste", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String teste(ModelMap map,
                        @RequestParam String[] key,
                        @RequestParam String[] value,
                        @RequestParam String ws_path) throws JsonProcessingException {
        map.addAttribute("teste", "Agora é um teste válido!");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        for(int i = 0; i < key.length; i++) {
            headers.add(key[i], value[i]);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(ws_path, HttpMethod.GET, request, String.class);

        //System.out.println(responseEntity.getBody());

        //ObjectMapper mapper = new ObjectMapper();
        //JsonNode json = mapper.readTree(responseEntity.getBody());

        //mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //String fJson = mapper.writeValueAsString(json);

        map.addAttribute("result", FileUtils.getFormattedJson(responseEntity.getBody()));

        //System.out.println(fJson);

        return "dataFragments :: #json-view";
    }

    @PostMapping("/config/ws/fields")
    public String getFields(ModelMap map,
                            @RequestParam String json) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        List<String> fields = new ArrayList<>();

        for (Iterator<String> it = jsonNode.fieldNames(); it.hasNext(); ) {
            String field = it.next();
            fields.add(field);

        }

        map.addAttribute("fields", fields);

        return "dataFragments :: #json-fields";
    }

    @PostMapping("/config/ws/save")
    public String save(ModelMap map,
                       @RequestParam(value = "enable", defaultValue = "false") boolean[] enable,
                       @RequestParam String[] originalName,
                       @RequestParam String[] newName,
                       @RequestParam String url,
                       @RequestParam String methodSelected) {

        Config config = new Config();
        List<Campo> campos = new ArrayList<>();

        for(int i = 0; i < enable.length; i++) {
            if (enable[i]) {
                Campo c = new Campo();
                c.setOriginalName(originalName[i]);
                c.setNewName(newName[i]);
                c.setConfig(config);

                campos.add(c);
            }
        }

        config.setId(1);
        config.setPath(url);
        config.setCampos(campos);

        List<Campo> cs = (List<Campo>) campoRepository.findAll();
        List<Config> cfs = (List<Config>) configRepository.findAll();

        for (Config c : cfs) {
            System.out.println("Id: " + c.getId());
            System.out.println("Url: " + c.getPath());
        }

        for(Campo c : cs) {
            System.out.println(c.getOriginalName());
        }

        Path root = Paths.get("config");
        String directoryName = "config";
        File directory = new File(directoryName);

        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream("config/integrador.config");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Config conf = (Config) objectInputStream.readObject();
            objectInputStream.close();

            System.out.println("URL do arquivo: " + conf.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("config/integrador.config");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(config);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //configRepository.save(config);

        return "dataFragments :: #save-complete";
    }
}
