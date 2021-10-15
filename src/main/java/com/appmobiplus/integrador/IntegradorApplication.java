package com.appmobiplus.integrador;

import com.appmobiplus.integrador.configuration.Config;
import com.appmobiplus.integrador.configuration.IntegrationType;
import com.appmobiplus.integrador.firebase.FirestoreConfig;
import com.appmobiplus.integrador.models.Produto;
import com.appmobiplus.integrador.repositories.ConfigRepository;
import com.appmobiplus.integrador.repositories.ProdutoRepository;
import com.appmobiplus.integrador.service.FileStorageService;
import com.appmobiplus.integrador.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EntityScan("com.appmobiplus.integrador.models")
@EnableJpaRepositories(basePackages = {"com.appmobiplus.integrador.repositories"})
@SpringBootApplication(scanBasePackages = { "com.appmobiplus.integrador"})
public class IntegradorApplication implements CommandLineRunner {
	@Resource
	FileStorageService storageService;

	@Autowired
	ConfigRepository configRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Value("${server.port}")
	private int port;

	public static void main(String[] args) {
		SpringApplication.run(IntegradorApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		LogUtils.saveLog("Servidor Iniciado...");

		//com.appmobiplus.integrador.firebase.Config c = new com.appmobiplus.integrador.firebase.Config();
		FirestoreConfig.initialize();

		ServerUtils.setPort(port);

		storageService.deleteAll();
		storageService.init();

		ImageUtils.downloadImageNews("https://imagens.ne10.uol.com.br/veiculos/_midias/jpg/2021/10/14/image_smmzjlc-19298998.jfif",
				"teste/teste/teste", "imgTeste", "jfif");

		Runnable compare = new Runnable() {
			@Override
			public void run() {
				compareFile();
			}
		};

		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		exec.scheduleAtFixedRate(compare, 0, 30, TimeUnit.SECONDS);
	}

	public void compareFile() {
		Config config = ConfigUtils.getCurrentConfig();
		if(config != null) {
			if(config.getIntegrationType() == IntegrationType.FILE) {
				long lastModifiedSaved = config.getFileLastModified();
				long lastModifiedNow = FileUtils.getFileLastModificationTime(config.getPath());
				if (lastModifiedNow > lastModifiedSaved) {
					try {
						List<Produto> produtos = FileUtils.getProdutos(config);

						produtoRepository.saveAll(produtos);

						config.setFileLastModified(lastModifiedNow);
						ConfigUtils.saveConfig(config);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Verificando arquivo");
			}
		} else {
			System.out.println("Não há configuração salva");
		}
	}
}
