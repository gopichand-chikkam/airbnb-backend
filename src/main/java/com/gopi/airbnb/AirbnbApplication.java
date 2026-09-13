package com.gopi.airbnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.PriorityQueue;

@SpringBootApplication
@EnableScheduling
public class AirbnbApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirbnbApplication.class, args);

	}

}
