package com.example.paymentapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PaymentAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentAppApplication.class, args);
	}

}
