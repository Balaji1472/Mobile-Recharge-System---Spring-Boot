package com.mrs.enpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MrsEndpointsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MrsEndpointsApplication.class, args);
	}

}
