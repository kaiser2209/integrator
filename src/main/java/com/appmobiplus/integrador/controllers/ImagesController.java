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
import java.util.Map;

@Controller
public class ImagesController {
    @Autowired
    ServletContext servletContext;

    @GetMapping(value = "/images/{image}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImages(@PathVariable("image") String image) throws IOException {
        InputStream in = getClass().getResourceAsStream(ImageUtils.getLocalPath() + image);
        return IOUtils.toByteArray(in);


    }

    @GetMapping(value = { "/midias/image/{var1}/{var2}/{var3}/{var4}/{var5}",
                            "/midias/image/{var1}/{var2}/{var3}/{var4}",
                            "/midias/image/{var1}/{var2}/{var3}",
                            "/midias/image/{var1}/{var2}",}, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getMedia(@PathVariable Map<String, String> var) throws IOException {

        String path = "";
        for(String key : var.keySet()) {
            path += "/" + var.get(key);
        }

        System.out.println(path);

        File file = new File("midias/image" + path);
        return IOUtils.toByteArray(file.toURI());
    }

    @GetMapping(value = { "/midias/news/{image}"}, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getMediaNews(@PathVariable String image) throws IOException {

        File file = new File("midias/news/" + image);
        return IOUtils.toByteArray(file.toURI());
    }

    @GetMapping(value = { "/midias/video/{var1}/{var2}/{var3}/{var4}/{var5}",
            "/midias/video/{var1}/{var2}/{var3}/{var4}",
            "/midias/video/{var1}/{var2}/{var3}",
            "/midias/video/{var1}/{var2}",})
    public @ResponseBody ResponseEntity getMediaVideo(@PathVariable Map<String, String> var) throws IOException {

        String path = "";
        for(String key : var.keySet()) {
            path += "/" + var.get(key);
        }

        System.out.println(path);

        File file = new File("midias/video" + path);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "video/" + "mp4")
                .body(IOUtils.toByteArray(file.toURI()));
        //return IOUtils.toByteArray(file.toURI());
    }

    @GetMapping(value = "/image/{image}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("image") String image) throws IOException {
        File file = new File(ImageUtils.getLocalPath() + image);
        return IOUtils.toByteArray(file.toURI());
    }
}
