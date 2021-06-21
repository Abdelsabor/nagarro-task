package com.nagarro.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nagarro.task"})
public class NagarroRestApplication {

	public static void main(String[] args) {

		SpringApplication.run(NagarroRestApplication.class, args);
	}
}
