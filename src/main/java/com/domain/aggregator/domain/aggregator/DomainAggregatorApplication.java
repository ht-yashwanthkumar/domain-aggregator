package com.domain.aggregator.domain.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DomainAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomainAggregatorApplication.class, args);
	}

}
