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

    //Página de busca de preço do produto
    @GetMapping(path="/buscapreco")
    public @ResponseBody Object get(@RequestParam Map<String, String> parameters) throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        //Carrega a configuração atual
        Config config = ConfigUtils.getCurrentConfig();

        //Declara o produto do pacote models
        com.appmobiplus.integrador.models.Produto p;

        //Mensagem para ser enviada ao log
        String message = "Busca realizada com ean=" + parameters.get("ean");
        LogUtils.saveLog(message);  //Armazena a mensagem configurada no log
        if (config.getIntegrationType() == IntegrationType.FILE) {  //Verifica se o tipo de integração é FILE (Arquivo)
            //Pega o parâmetro ean do link digitado
            String ean = parameters.get("ean");

            if (produtoRepository.existsByEan(ean)) {   //Verifica se o produto existe no banco de dados
                //Armazena o produto encontrado na variável p
                p = produtoRepository.findByEan(ean);

                if (p.getLink_image() == null || p.getLink_image().isEmpty()) { //Verifica se o parâmetro link_image está vazio
                    if (!ImageUtils.hasImageDownloaded(p.getEan(), "png")) {    //Verifica se a imagem correspondente ao ean já não foi baixa
                        if (ImageUtils.downloadImage(ImageUtils.getImageServerPath(), ImageUtils.getLocalPath(), p.getEan(), "png")) {  //Baixa a imagem referente ao ean retornando verdadeiro se o download foi bem sucedido
                            //Armazena o caminho da imagem no parâmetro link_image
                            p.setLink_image(ImageUtils.getImagePath(p.getEan(), "png"));
                            //Salva o produto no banco de dados
                            produtoRepository.save(p);
                        }
                    } else {    //Caso a imagem já tenha sido baixada anteriormente executa a instrução a seguir
                        //Armazena o caminho da imagem no parâmetro link_image
                        p.setLink_image(ImageUtils.getImagePath(p.getEan(), "png"));
                        //Salva o produto no banco de dados
                        produtoRepository.save(p);
                    }
                }

                //Mensagem de produto encontrado
                message = HttpStatus.OK + " - Produto encontado";
                //Envia a mensagem para o log
                LogUtils.saveLog(message);
                //Retorna o produto encontrado para a página
                return WebServiceUtils.getProdutoFromModel(p);
            }
        } else if (config.getIntegrationType() == IntegrationType.WEB_SERVICE) {    //Verifica se o tipo de integração é WEB_SERVICE
            if(config.getConfigAuth() == null) { //Verifica se a configuração de Autenticação não existe
                try {   //Tenta executar o código
                    //Recupera o header salvo na configuração
                    Set<Header> headers = config.getHeaders();
                    //Declaração da variável httpHeaders
                    HttpHeaders httpHeaders = new HttpHeaders();
                    for (Header h : headers) {
                        //Salva cada valor de header na variável httpHeaders
                        httpHeaders.add(h.getKey(), h.getValue());
                    }

                    //Cria a requisição a ser enviada
                    HttpEntity<String> request = new HttpEntity<>(httpHeaders);

                    //RestTemplate para manipulação de métodos HTTP
                    RestTemplate restTemplate = new RestTemplate();

                    for (String key : parameters.keySet()) {
                        //Armazena os parâmetros do url nas configurações
                        config.getParameters().put(key, parameters.get(key));
                    }

                    //Envia e armazena a resposta na variável responseEntity da url digitada
                    ResponseEntity<String> responseEntity = restTemplate.exchange(
                            WebServiceUtils.getWebServiceURL(config.getPath(), config.getParameters()), HttpMethod.GET,
                            request, String.class);

                    //Armazena o json presente no body do documento de resposta na variável finalJson
                    String finalJson = responseEntity.getBody();

                    //Recupera os campos salvos na configuração
                    Set<Field> fields = config.getFields();

                    //Instrução que altera os nomes dos campos para os novos nomes configurados
                    for (Field f : fields) {
                        if (!(f.getNewName().isEmpty() && f.getOriginalName().equals(f.getNewName()))) {
                            finalJson = finalJson.replaceAll(f.getOriginalName(), f.getNewName());
                        }
                    }

                    //Instancia um objeto mapper para tratamento de json
                    ObjectMapper mapper = new ObjectMapper();

                    //Converte o json recebido em um produto
                    Produto produto = mapper.readValue(finalJson, com.appmobiplus.integrador.configuration.Produto.class);

                    //Baixa a imagem e armazena o novo caminho no parâmetro link_image
                    produto.setLink_image(ImageUtils.downloadImage(produto.getLink_image(), produto.getEan(), ImageUtils.getLocalPath()));

                    //Salva as sugestões do produto em uma lista de produto
                    Set<Produto> sugestoes = produto.getSugestoes();
                    for (Produto product : sugestoes) {
                        //Baixa a imagem e armazena o novo caminho no parâmetro link_image
                        product.setLink_image(ImageUtils.downloadImage(product.getLink_image(), product.getEan(), ImageUtils.getLocalPath()));
                    }

                    //Mensagem de produto encontrado
                    message = HttpStatus.OK + " - Produto encontrado";
                    //Envia a mensagem para o log
                    LogUtils.saveLog(message);

                    //Retorna o produto para a página
                    return produto;
                } catch (HttpClientErrorException e) {  //Caso haja um exceção do tipo HttpClientErrorException
                    //Mensagem de produto não econtrado
                    message = HttpStatus.NOT_FOUND + " - Produto não encontrado";
                    //Envia a mensagem para o log
                    LogUtils.saveLog(message);
                    //Declara uma variável do tipo Map para armazenar informações do erro
                    Map<String, String> erro = new HashMap<>();
                    //Salva a chave errorMessage
                    erro.put("errorMessage", "Produto não econtrado!");
                    //Salva a chave errorCode
                    erro.put("errorCode", HttpStatus.NOT_FOUND.toString());

                    //Retorna o erro para a página
                    return erro;
                }
            } else {
                if (config.getConfigCustosProdutos() == null) { //Verifica se o Custo do Produto está definido
                    try {   //Tenta executar o código
                        //Declaração e instanciação de um novo objeto Produto
                        Produto produto = new Produto();

                        //Salva a chave codigoAcesso na lista de parâmetros
                        config.getConfigCadastroProdutos().getUrlParameters().put("codigoAcesso", parameters.get("ean"));

                        //System.out.println(Arrays.toString(config.getHeaders().toArray()));

                        //Retorna a url sem os parâmetros digitados
                        String basicUrl = WebServiceUtils.getAbsolutUrl(config.getConfigCadastroProdutos().getPath());

                        //Compõe a nova url com os parâmetros alterados
                        String url = WebServiceUtils.getWebServiceURL(basicUrl, config.getConfigCadastroProdutos().getUrlParameters());

                        //Salva a nova url nas configurações
                        config.getConfigCadastroProdutos().setPath(url);

                        HttpHeaders headers = ConfigUtils.getHttpHeaders(config.getHeaders());

                        //Verifica se o token de autenticação é válido e atualiza caso tenha expirado
                        ConfigUtils.verifyAndRenewToken(config.getHeaders(), config.getConfigCadastroProdutos(), config.getConfigAuth());

                        //Retorna json referente ao produto
                        String responseProduto = ConfigUtils.getResponse(config.getHeaders(), config.getConfigCadastroProdutos(), config.getConfigAuth()).getBody();

                        //Pega o primeiro item do json
                        JsonNode jsonProduto = JsonUtils.getJsonObject(responseProduto).get(0);

                        //Recupera os campos salvos na configuração
                        Set<Field> fields = config.getFields();

                        for (Field f : fields) {
                            if (jsonProduto.has(f.getOriginalName())) {
                                //Salva os campos no produto
                                produto.set(f.getNewName(), TypeUtils.getValue(jsonProduto.get(f.getOriginalName())));
                            }
                        }

                        //Atualiza a image do produto
                        ImageUtils.updateLinkImage(produto);

                        //Retorna o produto para a página
                        return produto;
                    } catch (NullPointerException e) { //Caso hava uma exceção do tipo NullPointException
                        //Declaração do variável erro e configuração dos campos erroMessage e errorCode
                        Map<String, String> erro = new HashMap<>();
                        erro.put("errorMessage", "Produto não econtrado!");
                        erro.put("errorCode", HttpStatus.NOT_FOUND.toString());

                        //Retorna o erro para a página
                        return erro;
                    }
                } else {
                    try {   //Tenta executar o código
                        //Atualiza os parâmetros de busca com o ean informado
                        config.getConfigCadastroProdutos().getSearchParameters().changeValue(Long.valueOf(parameters.get("ean")));

                        //Verifica se o token de autenticação é válido e atualiza caso tenha expirado
                        ConfigUtils.verifyAndRenewToken(config.getHeaders(), config.getConfigCadastroProdutos(), config.getConfigAuth());

                        //Retorna json referente ao produto
                        String responseProduto = ConfigUtils.getResponse(config.getHeaders(), config.getConfigCadastroProdutos(), config.getConfigAuth()).getBody();

                        int total = JsonUtils.getJsonObject(responseProduto).get("total").intValue();

                        //Declara e instancia um novo Produto
                        Produto produto = new Produto();

                        //Pega o primeiro objeto dentro de data
                        JsonNode jsonProduto = JsonUtils.getJsonObject(responseProduto).get("data").get(0);

                        //Recupera os parâmetros de busca
                        BuscaCadProdutos busca = config.getConfigCustosProdutos().getSearchParameters();

                        //Recupera o campo de busca
                        String field = busca.getCampo();

                        //Altera o valor do campo de busca
                        busca.changeValue(jsonProduto.get(field).longValue());


                        //Retorna json referente ao custo do produto
                        String responseCusto = ConfigUtils.getResponse(config.getHeaders(), config.getConfigCustosProdutos(), config.getConfigAuth()).getBody();

                        //Pega o primeiro objeto dentro de data
                        JsonNode jsonCusto = JsonUtils.getJsonObject(responseCusto).get("data").get(0);

                        //Recupera os campos salvos na configuração
                        Set<Field> fields = config.getFields();

                        //Instrução que salva as informações do produto
                        for (Field f : fields) {
                            if (jsonProduto.has(f.getOriginalName())) {
                                produto.set(f.getNewName(), TypeUtils.getValue(jsonProduto.get(f.getOriginalName())));
                            } else {
                                produto.set(f.getNewName(), TypeUtils.getValue(jsonCusto.get(f.getOriginalName())));
                            }
                        }

                        //Atualiza a imagem do produto
                        ImageUtils.updateLinkImage(produto);

                        //Retorna o produto para a página
                        return produto;

                    } catch (NullPointerException e) {
                        //Declara o mapa erro e define os campos
                        Map<String, String> erro = new HashMap<>();
                        erro.put("errorMessage", "Produto não econtrado!");
                        erro.put("errorCode", HttpStatus.NOT_FOUND.toString());

                        //Retorna o erro para a página
                        return erro;

                    }
                }

            }

        } else if (config.getIntegrationType() == IntegrationType.DATABASE) { //Verifica se o tipo de integração é DATABASE (Banco de Dados)

        }

        //Mensagem de produto não econtrado
        message = "Produto não encontrado! " + HttpStatus.NOT_FOUND;
        //Envia a mensagem para o log
        LogUtils.saveLog(message);
        //Lança uma exceção caso o produto não seja encontrado
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
