package com.weffy.common.config;

import com.weffy.common.kms.KmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    private final KmsService kmsService;
    private final String url;
    private final String username;
    private final String password;
    private final String driverClassName;

    public DataSourceConfiguration(KmsService kmsService,
                                   @Value("${spring.datasource.url}") String url,
                                   @Value("${spring.datasource.username}") String username,
                                   @Value("${spring.datasource.password}") String password,
                                   @Value("${spring.datasource.driver-class-name:}") String driverClassName) {
        this.kmsService = kmsService;
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
    }

    @Bean
    public DataSource dataSource() {
        if (url == null || username == null || password == null) {
            throw new IllegalArgumentException("Database configuration properties cannot be null.");
        }

        String decryptedDatabaseUrl;
        String decryptedDatabaseUsername;
        String decryptedDatabasePassword;
        try {
            decryptedDatabaseUrl = kmsService.decryptData(url);
            decryptedDatabaseUsername = kmsService.decryptData(username);
            decryptedDatabasePassword = kmsService.decryptData(password);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt database credentials using KMS.", e);
        }

        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
                .url(decryptedDatabaseUrl)
                .username(decryptedDatabaseUsername)
                .password(decryptedDatabasePassword);

        if (driverClassName != null && !driverClassName.trim().isEmpty()) {
            dataSourceBuilder.driverClassName(driverClassName);
        }

        return dataSourceBuilder.build();
    }
}
