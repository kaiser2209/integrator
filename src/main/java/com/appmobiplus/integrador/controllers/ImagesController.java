package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.utils.ImageUtils;
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

import javax.imageio.ImageIO;
import javax.persistence.Access;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class ImagesController {
    @Autowired
    ServletContext servletContext;

    @GetMapping(value = "/images/{image}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImages(@PathVariable("image") String image) throws IOException {
        InputStream in = getClass().getResourceAsStream(ImageUtils.getLocalPath() + image);
        return IOUtils.toByteArray(in);


    }

    @GetMapping(value = "/image/{image}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("image") String image) throws IOException {
        File file = new File(ImageUtils.getLocalPath() + image);
        return IOUtils.toByteArray(file.toURI());
    }
}
