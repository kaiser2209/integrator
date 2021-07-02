package com.appmobiplus.integrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EntityScan("com.appmobiplus.integrador.models")
@EnableJpaRepositories(basePackages = {"com.appmobiplus.integrador.repositories"})
@SpringBootApplication(scanBasePackages = { "com.appmobiplus.integrador"})
public class IntegradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegradorApplication.class, args);
	}
}
