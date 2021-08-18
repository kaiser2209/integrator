package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.configuration.*;
import com.appmobiplus.integrador.repositories.CampoRepository;
import com.appmobiplus.integrador.repositories.ConfigRepository;
import com.appmobiplus.integrador.utils.ConfigUtils;
import com.appmobiplus.integrador.utils.FileUtils;
import com.appmobiplus.integrador.utils.WebServiceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.*;

@Controller
public class WebServiceController {
    @Autowired
    ConfigRepository configRepository;

    @Autowired
    CampoRepository campoRepository;

    @PostMapping(value = "/config/ws/startConfig", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
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

        Map<String, String> mapHeader = new HashMap<>();
        for(int i = 0; i < key.length; i++) {
            mapHeader.put(key[i], value[i]);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonHeader = mapper.writeValueAsString(mapHeader);

        map.addAttribute("result", FileUtils.getFormattedJson(responseEntity.getBody()));
        map.addAttribute("jsonHeader", jsonHeader);

        return "dataFragments :: #json-view";
    }

    @PostMapping("/config/ws/fields")
    public String getFields(ModelMap map,
                            @RequestParam String json,
                            @RequestParam String header) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        List<String> fields = new ArrayList<>();

        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
        Map<String, String> mapHeader = mapper.readValue(header, typeRef);

        for (Iterator<String> it = jsonNode.fieldNames(); it.hasNext(); ) {
            String field = it.next();
            fields.add(field);

        }

        map.addAttribute("fields", fields);
        map.addAttribute("header", mapHeader);

        return "dataFragments :: #json-fields";
    }

    @PostMapping("/config/ws/save")
    public String save(ModelMap map,
                       @RequestParam(value = "enable", defaultValue = "false") boolean[] enable,
                       @RequestParam String[] originalName,
                       @RequestParam String[] newName,
                       @RequestParam String url,
                       @RequestParam String methodSelected,
                       @RequestParam IntegrationType integrationType,
                       @RequestParam String[] key,
                       @RequestParam String[] value) {

        Set<Field> fields = new HashSet<>();

        String absolutUrl = WebServiceUtils.getAbsolutUrl(url);
        Map<String, String> parameters = WebServiceUtils.getParameters(url);

        for(int i = 0; i < enable.length; i++) {
            if (enable[i]) {
                Field f = FieldBuilder.get()
                        .setOriginalName(originalName[i])
                        .setNewName(newName[i])
                        .build();

                fields.add(f);
            }
        }

        Set<Header> headers = new HashSet<>();
        for(int i = 0; i < key.length; i++) {
            Header h = new HeaderBuilder()
                    .setKey(key[i])
                    .setValue(value[i])
                    .build();

            headers.add(h);
        }

        Config config = ConfigBuilder.get()
                .setPath(absolutUrl)
                .setFields(fields)
                .setIntegrationType(integrationType)
                .setParameters(parameters)
                .setHeaders(headers)
                .build();

        ConfigUtils.saveConfig(config);

        return "dataFragments :: #save-complete";
    }

    @PostMapping(value = "/config/ws/auth/startConfig", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String authStartConfig() {
        return null;
    }
}
