package com.example.WorkforceIQ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class WorkforceIqApplication {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashed = encoder.encode("hr123");
        System.out.println("this ");
        System.out.println(hashed);
		SpringApplication.run(WorkforceIqApplication.class, args);
	}

}
