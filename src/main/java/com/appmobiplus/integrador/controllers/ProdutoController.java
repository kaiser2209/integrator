package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.configuration.*;
import com.appmobiplus.integrador.exceptions.ResourceNotFoundException;
import com.appmobiplus.integrador.json.ProdutoJson;
import com.appmobiplus.integrador.repositories.ProdutoRepository;
import com.appmobiplus.integrador.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class ProdutoController {
    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping(path="/add")
    public @ResponseBody String add(@RequestParam String name, @RequestParam String email) {
        com.appmobiplus.integrador.models.Produto p = new com.appmobiplus.integrador.models.Produto();
        p.setDescricao("Teste");
        p.setEan("12243242451515412");
        p.setPreco_de(2.50f);
        p.setPreco_por(2.50f);
        produtoRepository.save(p);


        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<com.appmobiplus.integrador.models.Produto> getAll() {
        return produtoRepository.findAll();
    }

    @GetMapping(path="/buscapreco")
    public @ResponseBody Object get(@RequestParam Map<String, String> parameters) throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        Config config = ConfigUtils.getCurrentConfig();
        com.appmobiplus.integrador.models.Produto p;
        String message = "Busca realizada com ean=" + parameters.get("ean");
        LogUtils.saveLog(message);
        if (config.getIntegrationType() == IntegrationType.FILE) {
            String ean = parameters.get("ean");
            if (produtoRepository.existsByEan(ean)) {
                p = produtoRepository.findByEan(ean);
                if (p.getLink_image() == null || p.getLink_image().isEmpty()) {
                    if (!ImageUtils.hasImageDownloaded(p.getEan(), "png")) {
                        if (ImageUtils.downloadImage(ImageUtils.getImageServerPath(), ImageUtils.getLocalPath(), p.getEan(), "png")) {
                            p.setLink_image(ImageUtils.getImagePath(p.getEan(), "png"));
                            produtoRepository.save(p);
                        }
                    } else {
                        p.setLink_image(ImageUtils.getImagePath(p.getEan(), "png"));
                        produtoRepository.save(p);
                    }
                }

                message = HttpStatus.OK + " - Produto encontado";
                LogUtils.saveLog(message);
                return WebServiceUtils.getProdutoFromModel(p);
            }
        } else if (config.getIntegrationType() == IntegrationType.WEB_SERVICE) {
            if(config.getConfigAuth() == null) {
                try {
                    Set<Header> headers = config.getHeaders();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    for (Header h : headers) {
                        httpHeaders.add(h.getKey(), h.getValue());
                    }

                    HttpEntity<String> request = new HttpEntity<>(httpHeaders);
                    RestTemplate restTemplate = new RestTemplate();

                    for (String key : parameters.keySet()) {
                        config.getParameters().put(key, parameters.get(key));
                    }

                    ResponseEntity<String> responseEntity = restTemplate.exchange(
                            WebServiceUtils.getWebServiceURL(config.getPath(), config.getParameters()), HttpMethod.GET,
                            request, String.class);

                    String finalJson = responseEntity.getBody();

                    Set<Field> fields = config.getFields();

                    for (Field f : fields) {
                        if (!(f.getNewName().isEmpty() && f.getOriginalName().equals(f.getNewName()))) {
                            finalJson = finalJson.replaceAll(f.getOriginalName(), f.getNewName());
                        }
                    }

                    ObjectMapper mapper = new ObjectMapper();

                    Produto produto = mapper.readValue(finalJson, com.appmobiplus.integrador.configuration.Produto.class);
                    produto.setLink_image(ImageUtils.downloadImage(produto.getLink_image(), produto.getEan(), ImageUtils.getLocalPath()));

                    Set<Produto> sugestoes = produto.getSugestoes();
                    for (Produto product : sugestoes) {
                        product.setLink_image(ImageUtils.downloadImage(product.getLink_image(), product.getEan(), ImageUtils.getLocalPath()));
                    }

                    message = HttpStatus.OK + " - Produto encontrado";
                    LogUtils.saveLog(message);
                    return produto;
                } catch (HttpClientErrorException e) {
                    message = HttpStatus.NOT_FOUND + " - Produto não encontrado";
                    LogUtils.saveLog(message);
                    Map<String, String> erro = new HashMap<>();
                    erro.put("errorMessage", "Produto não econtrado!");
                    erro.put("errorCode", HttpStatus.NOT_FOUND.toString());

                    return erro;
                }
            } else {
                if (config.getConfigCustosProdutos() == null) {
                    Produto produto = new Produto();

                    config.getConfigCadastroProdutos().getUrlParameters().put("codigoAcesso", parameters.get("ean"));

                    String responseProduto = TestUtils.getJsonProductTest();

                    JsonNode jsonProduto = JsonUtils.getJsonObject(responseProduto).get(0);

                    Set<Field> fields = config.getFields();

                    for(Field f : fields) {
                        if(jsonProduto.has(f.getOriginalName())) {
                            produto.set(f.getNewName(), TypeUtils.getValue(jsonProduto.get(f.getOriginalName())));
                        }
                    }

                    return produto;
                } else {
                    try {
                        config.getConfigCadastroProdutos().getSearchParameters().changeValue(Long.valueOf(parameters.get("ean")));
                        ConfigUtils.verifyAndRenewToken(config.getHeaders(), config.getConfigCadastroProdutos(), config.getConfigAuth());
                        String responseProduto = ConfigUtils.getResponse(config.getHeaders(), config.getConfigCadastroProdutos(), config.getConfigAuth()).getBody();

                        int total = JsonUtils.getJsonObject(responseProduto).get("total").intValue();

                        Produto produto = new Produto();

                        JsonNode jsonProduto = JsonUtils.getJsonObject(responseProduto).get("data").get(0);

                        BuscaCadProdutos busca = config.getConfigCustosProdutos().getSearchParameters();
                        String field = busca.getCampo();

                        busca.changeValue(jsonProduto.get(field).longValue());

                        String responseCusto = ConfigUtils.getResponse(config.getHeaders(), config.getConfigCustosProdutos(), config.getConfigAuth()).getBody();

                        JsonNode jsonCusto = JsonUtils.getJsonObject(responseCusto).get("data").get(0);

                        Set<Field> fields = config.getFields();

                        for (Field f : fields) {
                            if (jsonProduto.has(f.getOriginalName())) {
                                produto.set(f.getNewName(), TypeUtils.getValue(jsonProduto.get(f.getOriginalName())));
                            } else {
                                produto.set(f.getNewName(), TypeUtils.getValue(jsonCusto.get(f.getOriginalName())));
                            }
                        }

                        return produto;

                    } catch (NullPointerException e) {
                        Map<String, String> erro = new HashMap<>();
                        erro.put("errorMessage", "Produto não econtrado!");
                        erro.put("errorCode", HttpStatus.NOT_FOUND.toString());

                        return erro;
                    }
                }

            }

        } else if (config.getIntegrationType() == IntegrationType.DATABASE) {

        }

        message = "Produto não encontrado! " + HttpStatus.NOT_FOUND;
        LogUtils.saveLog(message);
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

        String newJson = responseEntity.getBody().replaceAll("precoDe", "preco_de");
        newJson = newJson.replaceAll("precoPor", "preco_por");

        responseEntity.getBody().replaceAll("precoDe", "preco_de");
        responseEntity.getBody().replaceAll("precoPor", "preco_por");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(newJson);

        ObjectNode returnJson = new ObjectMapper().createObjectNode();
        returnJson.set("preco_de", actualObj.get("precoDe"));
        returnJson.set("sugeridos", actualObj.get("sugeridos"));

        return actualObj;
    }
}
