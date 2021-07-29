package com.appmobiplus.integrador.controllers;

import com.appmobiplus.integrador.exceptions.ResourceNotFoundException;
import com.appmobiplus.integrador.models.Produto;
import com.appmobiplus.integrador.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
}
