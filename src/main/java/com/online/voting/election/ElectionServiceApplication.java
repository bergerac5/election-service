package com.online.voting.election;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "com.online.voting.election.clients")
public class ElectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectionServiceApplication.class, args);
	}

}
