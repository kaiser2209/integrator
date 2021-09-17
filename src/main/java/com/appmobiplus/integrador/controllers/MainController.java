package com.appmobiplus.integrador.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {
    @GetMapping("/")
    private String home() {
        return "flutter/index";
    }

    //Mostra a página de configurações
    @GetMapping("/config")
    private String config() {
        return "home";
    }

    //Mostra a página de configurações de arquivo
    @GetMapping("/config/file")
    private String file() {
        //return "fileConfig";
        return "fileConfiguration";
    }

    //Mostra a página de configurações de web service
    @GetMapping("/config/ws")
    private String webService() {
        return "wsConfig";
    }

}
