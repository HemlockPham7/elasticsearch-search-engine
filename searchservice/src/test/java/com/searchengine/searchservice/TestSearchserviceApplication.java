package com.searchengine.searchservice;

import org.springframework.boot.SpringApplication;

public class TestSearchserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(SearchserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
