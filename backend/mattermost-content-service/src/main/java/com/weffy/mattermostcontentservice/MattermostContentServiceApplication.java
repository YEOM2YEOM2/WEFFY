package com.weffy.mattermostcontentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MattermostContentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MattermostContentServiceApplication.class, args);
	}

}
