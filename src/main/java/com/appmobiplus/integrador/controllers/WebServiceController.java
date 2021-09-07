package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.configuration.*;
import com.appmobiplus.integrador.repositories.CampoRepository;
import com.appmobiplus.integrador.repositories.ConfigRepository;
import com.appmobiplus.integrador.utils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
                        @RequestParam String ws_path) {
        map.addAttribute("teste", "Agora é um teste válido!");

        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            for (int i = 0; i < key.length; i++) {
                headers.add(key[i], value[i]);
            }

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(ws_path, HttpMethod.GET, request, String.class);

            Map<String, String> mapHeader = new HashMap<>();
            for (int i = 0; i < key.length; i++) {
                mapHeader.put(key[i], value[i]);
            }

            ObjectMapper mapper = new ObjectMapper();
            String jsonHeader = mapper.writeValueAsString(mapHeader);
            map.addAttribute("result", FileUtils.getFormattedJson(responseEntity.getBody()));
            map.addAttribute("jsonHeader", jsonHeader);

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:67");
        }

            return "dataFragments :: #json-view";

    }

    @PostMapping("/config/ws/fields")
    public String getFields(ModelMap map,
                            @RequestParam String json,
                            @RequestParam String header) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);

            List<String> fields = new ArrayList<>();

            TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
            };
            Map<String, String> mapHeader = mapper.readValue(header, typeRef);

            for (Iterator<String> it = jsonNode.fieldNames(); it.hasNext(); ) {
                String field = it.next();
                fields.add(field);

            }

            map.addAttribute("fields", fields);
            map.addAttribute("header", mapHeader);

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:100");
        }

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

        try {

            Set<Field> fields = new HashSet<>();

            String absolutUrl = WebServiceUtils.getAbsolutUrl(url);
            Map<String, String> parameters = WebServiceUtils.getParameters(url);

            for (int i = 0; i < enable.length; i++) {
                if (enable[i]) {
                    Field f = FieldBuilder.get()
                            .setOriginalName(originalName[i])
                            .setNewName(newName[i])
                            .build();

                    fields.add(f);
                }
            }

            Set<Header> headers = new HashSet<>();
            for (int i = 0; i < key.length; i++) {
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
            LogUtils.saveLog("Configurações salvas!");

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() +  " - WebServiceController.java:156");
        }

        return "dataFragments :: #save-complete";
    }

    @PostMapping(value = "/config/ws/auth/startConfig", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String authStartConfig(ModelMap map,
                                  @RequestParam HttpMethod method,
                                  @RequestParam String ws_path,
                                  @RequestParam String[] headerKey,
                                  @RequestParam String[] headerValue,
                                  @RequestParam String[] bodyKey,
                                  @RequestParam String[] bodyValue,
                                  @RequestParam(value = "auth-bodyType") String authBodyType,
                                  @RequestParam String[] bodyKeyRaw,
                                  @RequestParam String[] bodyValueRaw,
                                  @RequestParam String[] bodyTypeRaw) throws JsonProcessingException {

        //bodyKey = TestUtils.getAuthTestKeys();
        //bodyValue = TestUtils.getAuthTestValues();
        //ws_path = TestUtils.getAuthUrl();

        Map<String, Object> mapBodyRaw = new HashMap<>();

        if (bodyKeyRaw.length > 0) {
            for(int i = 0; i < bodyKeyRaw.length; i++) {
                if (bodyTypeRaw[i].equals("number")) {
                    mapBodyRaw.put(bodyKeyRaw[i], Long.valueOf(bodyValueRaw[i]));
                } else {
                    mapBodyRaw.put(bodyKeyRaw[i], bodyValueRaw[i]);
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonBodyRaw = mapper.writeValueAsString(mapBodyRaw);

        System.out.println(jsonBodyRaw);

        System.out.println(Arrays.toString(bodyKeyRaw));
        System.out.println(Arrays.toString(bodyValueRaw));
        System.out.println(Arrays.toString(bodyTypeRaw));
        System.out.println(authBodyType);

        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            if(authBodyType.equals("raw")) {
                headers.setContentType(MediaType.APPLICATION_JSON);
            } else {
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            }

            MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
            for (int i = 0; i < bodyKey.length; i++) {
                postParameters.add(bodyKey[i], bodyValue[i]);
            }

            ResponseEntity<String> response;

            if (authBodyType.equals("raw")) {
                HttpEntity<String> request = new HttpEntity<>(jsonBodyRaw, headers);
                response = restTemplate.exchange(ws_path, method, request, String.class);
            } else {
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(postParameters, headers);
                response = restTemplate.exchange(ws_path, method, request, String.class);
            }

            map.addAttribute("result", FileUtils.getFormattedJson(response.getBody()));

            Map<String, String> authBodyKeys = new HashMap<>();
            for (int i = 0; i < bodyKey.length; i++) {
                authBodyKeys.put(bodyKey[i], bodyValue[i]);
            }

            ConfigUtils.setConfig(new ConfigBuilder()
                    .setConfigAuth(new ConfigAuthBuilder()
                            .setMethodType(method)
                            .setPath(ws_path)
                            .setBodyParameters(authBodyKeys)
                            .build())
                    .build());

            return "dataFragments :: #auth-jsonView";
        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:206");
            LogUtils.saveLog(Arrays.toString(e.getStackTrace()));
            map.addAttribute("title", "Erro");
            map.addAttribute("errorMessage", e.getMessage());
            return "dataFragments :: #dialog-error";
        }


    }

    @PostMapping(value = "/config/ws/auth/configAuth", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String configAuth(ModelMap map,
                             @RequestParam String json) throws JsonProcessingException {

        try {

            JsonNode jsonNode = JsonUtils.getJsonObject(json);

            List<String> fields = JsonUtils.getJsonFields(jsonNode);

            map.addAttribute("fields", fields);
            map.addAttribute("authJsonFields", json);
        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:225");
        }

        return "dataFragments :: #auth-jsonFields";
    }

    @PostMapping(value = "/config/ws/cad/startConfig", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String configProd(ModelMap map,
                             @RequestParam(value= "key", defaultValue = "") String key,
                             @RequestParam(value = "value", defaultValue = "") String[] value,
                             @RequestParam(value = "authJson", defaultValue = "") String authJson) throws JsonProcessingException {

        try {
            String authorizationValue = "";

            JsonNode json = JsonUtils.getJsonObject(authJson);

            for (String v : value) {
                authorizationValue += json.get(v).textValue() + " ";
            }

            authorizationValue = authorizationValue.trim();

            map.addAttribute("headerKey", key);
            map.addAttribute("headerValue", authorizationValue);

            ConfigUtils.getConfig().getConfigAuth().setFieldsUsedInAuth(value);
            ConfigUtils.getConfig().getConfigAuth().setAuthField(key);

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:256");

        }

        return "dataFragments :: #cad-config";
    }

    @PostMapping(value = "/config/ws/cad/cadConfig", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String confCadProd(ModelMap map,
                              @RequestParam String ws_path,
                              @RequestParam HttpMethod method,
                              @RequestParam String[] key,
                              @RequestParam String[] value,
                              @RequestParam String campo,
                              @RequestParam(defaultValue = "0") long valor,
                              @RequestParam String operador) throws JsonProcessingException {

        try {

            ConfigCadastroProduto getCadastro = new ConfigCadastroProdutoBuilder()
                    .setPath(ws_path)
                    .setMethod(method)
                    .setHeader(ConfigUtils.getMapParameters(key, value))
                    .addBody("campo", campo)
                    .addBody("valor", String.valueOf(valor))
                    .addBody("operador", operador)
                    .build();

            //System.out.println(getCadastro.toString());

            Map<String, String> urlParameters = WebServiceUtils.getParameters(ws_path);

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();

            for (int i = 0; i < key.length; i++) {
                headers.add(key[i], value[i]);
            }

            BuscaCadProdutos buscaCadProdutos = new BuscaCadProdutos();
            buscaCadProdutos.setPage(1);
            buscaCadProdutos.addClausula(campo, valor, operador);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(buscaCadProdutos);

            HttpEntity<String> request = new HttpEntity<>(json, headers);

            ResponseEntity<String> response = restTemplate.exchange(ws_path, method, request, String.class);

            ConfigUtils.getConfig().setConfigCadastroProdutos(new ConfigCadastroProdutosBuilder()
                    .setPath(ws_path)
                    .setMethod(method)
                    .setSearchParameters(buscaCadProdutos)
                    .setUrlParameters(urlParameters)
                    .build());

            Set<Header> configHeaders = new HashSet<>();
            for (int i = 0; i < key.length; i++) {
                Header h = HeaderBuilder.get()
                        .addKeySet(key[i], value[i])
                        .build();
                configHeaders.add(h);
            }

            ConfigUtils.getConfig().setHeaders(configHeaders);

            map.addAttribute("result", FileUtils.getFormattedJson(response.getBody()));
            map.addAttribute("headers", headers);

            return "dataFragments :: #cadJsonView";

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:312");
            LogUtils.saveLog(Arrays.toString(e.getStackTrace()));
            map.addAttribute("title", "Erro");
            map.addAttribute("errorMessage", e.getMessage());
            return "dataFragments :: #dialog-error";
        }

    }

    @PostMapping(value = "/config/ws/cad/loadFields", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String loadFields(ModelMap map,
                                  @RequestParam String json,
                                  @RequestParam String[] headerKey,
                                  @RequestParam String[] headerValue) throws JsonProcessingException {

        try {

            Map<String, String> headerMap = new HashMap<>();

            for (int i = 0; i < headerKey.length; i++) {
                headerMap.put(headerKey[i], headerValue[i]);
            }

            JsonNode jsonNode = JsonUtils.getJsonObject(json);

            List<String> fields = JsonUtils.getJsonFields(jsonNode.get("data").get(0));

            map.addAttribute("fields", fields);
            map.addAttribute("headers", headerMap);
            map.addAttribute("json", json);
        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:340");
        }

        return "dataFragments :: #cad-findValueContent";
    }

    @PostMapping(value = "/config/ws/cad/loadFieldsValues", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String loadFieldsValues(ModelMap map,
                                   @RequestParam HttpMethod method,
                                   @RequestParam String ws_path,
                                   @RequestParam String campo,
                                   @RequestParam(defaultValue = "0") long valor,
                                   @RequestParam String operador,
                                   @RequestParam String jsonProduto,
                                   @RequestParam String[] key,
                                   @RequestParam String[] value) throws JsonProcessingException {

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();

            for (int i = 0; i < key.length; i++) {
                httpHeaders.add(key[i], value[i]);
            }

            BuscaCadProdutos buscaCadProdutos = new BuscaCadProdutos();
            buscaCadProdutos.setPage(1);
            buscaCadProdutos.addClausula(campo, valor, operador);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(buscaCadProdutos);

            System.out.println(json);

            HttpEntity<String> request = new HttpEntity<>(json, httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(ws_path, method, request, String.class);

            map.addAttribute("result", FileUtils.getFormattedJson(response.getBody()));

            JsonNode jsonNodeProduto = JsonUtils.getJsonObject(jsonProduto);
            JsonNode jsonNodeCusto = JsonUtils.getJsonObject(FileUtils.getFormattedJson(response.getBody()));

            List<String> fields = JsonUtils.getJsonFields(jsonNodeProduto.get("data").get(0));
            List<String> fieldsCusto = JsonUtils.getJsonFields(jsonNodeCusto.get("data").get(0));
            List<String> fieldsToAdd = new ArrayList<>();

            for (String f : fieldsCusto) {
                if (!fields.contains(f)) {
                    fieldsToAdd.add(f);
                }
            }

            ConfigUtils.getConfig().setConfigCustosProdutos(new ConfigCustosProdutosBuilder()
                    .setPath(ws_path)
                    .setSearchParameters(buscaCadProdutos)
                    .setMethod(method)
                    .build());

            fields.addAll(fieldsToAdd);

            map.addAttribute("fields", fields);

            return "dataFragments :: #cadFieldsValues";

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:404");
            LogUtils.saveLog(Arrays.toString(e.getStackTrace()));
            map.addAttribute("title", "Erro");
            map.addAttribute("errorMessage", e.getMessage());
            return "dataFragments :: #dialog-error";
        }


    }

    @PostMapping(value = "/config/ws/cad/saveConfig", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String loadFieldsValues(ModelMap map,
                                   @RequestParam IntegrationType integrationType,
                                   @RequestParam String[] originalName,
                                   @RequestParam String[] newName) {

        try {

            ConfigUtils.getConfig().setIntegrationType(integrationType);

            Set<Field> fields = new HashSet<>();

            for (int i = 0; i < originalName.length; i++) {
                if (!originalName[i].equals("0")) {
                    fields.add(FieldBuilder.get()
                            .setOriginalName(originalName[i])
                            .setNewName(newName[i])
                            .build());
                }
            }

            ConfigUtils.getConfig().setFields(fields);
            ConfigUtils.saveConfig();

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage() + " - WebServiceController.java:435");
        }

        return "dataFragments :: #save-complete";
    }
}
