package com.appmobiplus.integrador.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.util.Map;

@Controller
public class WebServiceController {

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

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseEntity.getBody());

        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        String fJson = mapper.writeValueAsString(json);

        map.addAttribute("result", fJson);

        System.out.println(fJson);

        return "wsConfig :: #json";
    }
}
