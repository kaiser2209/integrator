package com.appmobiplus.integrador.repositories;


import com.appmobiplus.integrador.models.Produto;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepository extends CrudRepository<Produto, Integer> {
    Produto findByEan(String ean);
}
