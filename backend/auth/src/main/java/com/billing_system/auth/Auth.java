package com.billing_system.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Auth {

	public static void main(String[] args) {
		SpringApplication.run(Auth.class, args);
	}

}
