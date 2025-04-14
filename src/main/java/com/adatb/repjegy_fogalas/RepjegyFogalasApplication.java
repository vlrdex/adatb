package com.adatb.repjegy_fogalas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class RepjegyFogalasApplication {

	private final DataSource dataSource;

	@Autowired
	public RepjegyFogalasApplication(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public static void main(String[] args) {
		SpringApplication.run(RepjegyFogalasApplication.class, args);
	}

}
