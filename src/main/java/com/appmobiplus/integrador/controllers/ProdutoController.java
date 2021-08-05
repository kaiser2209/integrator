package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.exceptions.ResourceNotFoundException;
import com.appmobiplus.integrador.json.ProdutoJson;
import com.appmobiplus.integrador.models.Produto;
import com.appmobiplus.integrador.repositories.ProdutoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProdutoController {
    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping(path="/add")
    public @ResponseBody String add(@RequestParam String name, @RequestParam String email) {
        Produto p = new Produto();
        p.setDescricao("Teste");
        p.setEan("12243242451515412");
        p.setPreco_de(2.50f);
        p.setPreco_por(2.50f);
        produtoRepository.save(p);


        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Produto> getAll() {
        return produtoRepository.findAll();
    }

    @GetMapping(path="/buscapreco")
    public @ResponseBody Produto get(@RequestParam String ean) {
        if (produtoRepository.existsByEan(ean)) {
            return produtoRepository.findByEan(ean);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O recurso procurado não foi encontrado!");
    }

    @GetMapping(path="/buscapreco/ws")
    public @ResponseBody JsonNode get(@RequestParam String ean, @RequestParam String filial) throws NoSuchFieldException, IllegalAccessException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://190.15.121.162:9202/rest/CK100INTEGRATION/CONSULTAPRECO?EAN=" + ean +
                "&filial=" + filial;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", "Vp10a@(vi!iF0!9CZa!%L");
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        //Object object = responseEntity.getBody();

        //Field field = object.getClass().getDeclaredField("ean");
        //field.setAccessible(true);
        //Object value = field.get(object);

        //System.out.println(object);

        String newJson = responseEntity.getBody().replaceAll("precoDe", "preco_de");
        newJson = newJson.replaceAll("precoPor", "preco_por");

        responseEntity.getBody().replaceAll("precoDe", "preco_de");
        responseEntity.getBody().replaceAll("precoPor", "preco_por");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(newJson);

        System.out.println(actualObj);

        ObjectNode returnJson = new ObjectMapper().createObjectNode();
        returnJson.set("preco_de", actualObj.get("precoDe"));
        returnJson.set("sugeridos", actualObj.get("sugeridos"));

        return actualObj;
    }
}
