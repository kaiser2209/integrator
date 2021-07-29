package com.appmobiplus.integrador.repositories;


import com.appmobiplus.integrador.models.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepository extends CrudRepository<Produto, Integer> {
    Produto findByEan(String ean);
    boolean existsByEan(String ean);
    @Query(value = "ALTER TABLE Produto ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetId();
}
