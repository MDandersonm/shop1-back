package com.example.shop1back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // JPA Auditing 활성화
public class Shop1BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(Shop1BackApplication.class, args);
	}

}
