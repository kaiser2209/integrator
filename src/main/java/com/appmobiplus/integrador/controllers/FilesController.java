package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.configuration.Config;
import com.appmobiplus.integrador.configuration.ConfigBuilder;
import com.appmobiplus.integrador.configuration.Field;
import com.appmobiplus.integrador.configuration.FieldBuilder;
import com.appmobiplus.integrador.configuration.IntegrationType;
import com.appmobiplus.integrador.message.ResponseMessage;
import com.appmobiplus.integrador.models.*;
import com.appmobiplus.integrador.repositories.ConfigRepository;
import com.appmobiplus.integrador.repositories.ProdutoRepository;
import com.appmobiplus.integrador.service.FileStorageService;
import com.appmobiplus.integrador.utils.ConfigUtils;
import com.appmobiplus.integrador.utils.FileUtils;
import com.appmobiplus.integrador.utils.LogUtils;
import org.apache.coyote.Response;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:4250")
public class FilesController {

    @Autowired
    FileStorageService storageService;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam ("file")MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);

            byte[] bytes = file.getBytes();
            String arquivo = new String(bytes);
            String[] linhas = arquivo.split("#");

            System.out.println(arquivo);
            for (String l : linhas) {
                System.out.println(l);
            }

            message = "Upload the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping("/config/file/send")
    public ModelAndView upload(@RequestParam ("path")String path,
                               @RequestParam ("integrationType") String integrationType) {
        String message = "";
        List<String> preview = new ArrayList<>();
        try {
            preview = FileUtils.lines(path, 6);
            message = "Salvo com Sucesso!";
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", message);
            modelAndView.addObject("preview", preview);
            modelAndView.addObject("path", path);
            modelAndView.addObject("integrationType", integrationType);
            modelAndView.setViewName("filePreview");
            LogUtils.saveLog("Configuração iniciada. Tipo:" + integrationType);
            LogUtils.saveLog("Caminho do arquivo definido: " + path);
            return modelAndView;

        } catch (Exception e) {
            LogUtils.saveLog(e.getMessage());
            message = "Erro";
            return null;
        }
    }

    @PostMapping("/config/file/send/preview")
    public ModelAndView uploadPreview(@RequestParam("path") String path,
                                      @RequestParam("integrationType") String integrationType,
                                      @RequestParam(value = "hasDelimiter", defaultValue = "false") boolean hasDelimiter,
                                      @RequestParam("delimiter") String delimiter,
                                      @RequestParam("fields") String[] fields,
                                      @RequestParam(value = "posBegin", defaultValue = "0") int[] posBegin,
                                      @RequestParam(value = "posEnd", defaultValue = "0") int[] posEnd,
                                      @RequestParam("fieldPrice") String fieldPrice,
                                      @RequestParam("decimalPoint") int decimalPoint) {

        List<Produto> produtos = new ArrayList<>();
        try {
            produtos = FileUtils.getProdutos(path, hasDelimiter, delimiter, fields, posBegin, posEnd, fieldPrice, decimalPoint);
            LogUtils.saveLog("Lista de produtos recuperada do arquivo.");
        } catch (IOException e) {
            LogUtils.saveLog(e.getMessage() + " - FilesController.java:112");
            e.printStackTrace();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("path", path);
        modelAndView.addObject("integrationType",integrationType);
        modelAndView.addObject("hasDelimiter", hasDelimiter);
        modelAndView.addObject("delimiter", delimiter);
        modelAndView.addObject("fields", fields);
        modelAndView.addObject("posBegin", posBegin);
        modelAndView.addObject("posEnd", posEnd);
        modelAndView.addObject("fieldPrice", fieldPrice);
        modelAndView.addObject("decimalPoint", decimalPoint);
        modelAndView.addObject("produtos", produtos);
        modelAndView.setViewName("filePreviewSend");

        return modelAndView;
    }

    @PostMapping("/config/file/send/save")
    public ModelAndView save(@RequestParam("path") String path,
                             @RequestParam("integrationType") IntegrationType integrationType,
                             @RequestParam(value = "hasDelimiter", defaultValue = "false") boolean hasDelimiter,
                             @RequestParam("delimiter") String delimiter,
                             @RequestParam("fields") String[] fields,
                             @RequestParam(value = "posBegin", defaultValue = "0") int[] posBegin,
                             @RequestParam(value = "posEnd", defaultValue = "0") int[] posEnd,
                             @RequestParam("fieldPrice") String fieldPrice,
                             @RequestParam("decimalPoint") int decimalPoint) {

        ModelAndView modelAndView = new ModelAndView();

        Set<Field> allFields = new HashSet<>();

        for(int i = 0; i < fields.length; i++) {
            Field f = FieldBuilder.get()
                    .setOriginalName(fields[i])
                    .setNewName(fields[i])
                    .setPosBegin(posBegin[i])
                    .setPosEnd(posEnd[i])
                    .setCurrencyField(fields[i].equals(fieldPrice))
                    .build();

            if(fields[i].equals(fieldPrice)) {
                f.setDecimalPoint(decimalPoint);
            }

            allFields.add(f);
        }

        Config config = ConfigBuilder.get()
                .hasDelimiter(hasDelimiter)
                .setDelimiter(delimiter)
                .setIntegrationType(integrationType)
                .setPath(path)
                .setFileLastModified(FileUtils.getFileLastModificationTime(path))
                .setFields(allFields)
                .build();

        ConfigUtils.saveConfig(config);
        LogUtils.saveLog("Configurações salvas com sucesso. Tipo:" + integrationType);

        List<Produto> produtos = new ArrayList<>();
        try {
            produtos = FileUtils.getProdutos(path, hasDelimiter, delimiter, fields, posBegin, posEnd, fieldPrice, decimalPoint);
        } catch (IOException e) {
            LogUtils.saveLog(e.getMessage() + this.getClass().getName());
            e.printStackTrace();
        }

        produtoRepository.deleteAll();
        produtoRepository.saveAll(produtos);
        LogUtils.saveLog("Lista de produtos salva com sucesso!");

        modelAndView.setViewName("fileSave");

        return modelAndView;

    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + file.getFilename() + "\"").body(file);
    }
}
