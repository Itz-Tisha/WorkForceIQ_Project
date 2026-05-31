package com.example.WorkforceIQ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WorkforceIqApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkforceIqApplication.class, args);
	}

}
