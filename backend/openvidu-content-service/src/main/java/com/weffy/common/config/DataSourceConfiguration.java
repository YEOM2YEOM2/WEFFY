package com.weffy.common.config;

import com.weffy.common.kms.KmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Autowired
    private KmsService kmsService;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean // dataSource를 빈으로 등록
    public DataSource dataSource() {
        String decryptedDatabaseUrl = kmsService.decryptData(url);
        String decryptedDatabaseUsername = kmsService.decryptData(username);
        String decryptedDatabasePassword = kmsService.decryptData(password);

        return DataSourceBuilder.create()
                .url(decryptedDatabaseUrl)
                .username(decryptedDatabaseUsername)
                .password(decryptedDatabasePassword)
                // .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}

