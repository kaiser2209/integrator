package com.appmobiplus.integrador.repositories;

import com.appmobiplus.integrador.models.Config;
import org.springframework.data.repository.CrudRepository;

public interface ConfigRepository extends CrudRepository<Config, Integer> {
    boolean existsById(long Id);
    Config findById(long Id);
}
