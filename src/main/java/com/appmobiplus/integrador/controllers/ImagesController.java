package com.appmobiplus.integrador.controllers;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.ServletContextResource;

import javax.persistence.Access;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImagesController {
    @Autowired
    ServletContext servletContext;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @GetMapping(value = "/image/{image}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("image") String image) throws IOException {
        //HttpHeaders headers = new HttpHeaders();
        //Resource resource = new ServletContextResource(servletContext, "download/images/7891025107897.png");
        //return resourceLoader.getResource("classpath:download/images/7891025107897.png");

        InputStream in = getClass().getResourceAsStream("../../../../download/images/" + image);
        return IOUtils.toByteArray(in);

        //return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
