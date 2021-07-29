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

    @GetMapping("/config")
    private String config() {
        return "home";
    }

    @GetMapping("/config/file")
    private String file() {
        //return "fileConfig";
        return "fileConfiguration";
    }

}
