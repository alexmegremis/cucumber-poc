package com.alexmegremis.cucumberPOC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@EntityScan(basePackages = {"com.alexmegremis.cucumberPOC"})
@EnableJpaRepositories
@SpringBootApplication
public class CucumberPOCApplication {

	public static void main(String[] args) {
		SpringApplication.run(CucumberPOCApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
