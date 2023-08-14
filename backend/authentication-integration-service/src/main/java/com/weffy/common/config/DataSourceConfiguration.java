//package com.weffy.common.config;
//
//import com.weffy.common.kms.KmsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfiguration {
//
//    private final KmsService kmsService;
//    private final String url;
//    private final String username;
//    private final String password;
//
//    public DataSourceConfiguration(KmsService kmsService,
//                                   @Value("${spring.datasource.url}") String url,
//                                   @Value("${spring.datasource.username}") String username,
//                                   @Value("${spring.datasource.password}") String password) {
//        this.kmsService = kmsService;
//        this.url = url;
//        this.username = username;
//        this.password = password;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        if (url == null || username == null || password == null) {
//            throw new IllegalArgumentException("Database configuration properties cannot be null.");
//        }
//
//        String decryptedDatabaseUrl;
//        String decryptedDatabaseUsername;
//        String decryptedDatabasePassword;
//        try {
//            decryptedDatabaseUrl = kmsService.decryptData(url);
//            System.out.println(decryptedDatabaseUrl);
//            decryptedDatabaseUsername = kmsService.decryptData(username);
//            System.out.println(decryptedDatabaseUsername);
//            decryptedDatabasePassword = kmsService.decryptData(password);
//            System.out.println(decryptedDatabasePassword);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to decrypt database credentials using KMS.", e);
//        }
//
//        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
//                .url(decryptedDatabaseUrl)
//                .username(decryptedDatabaseUsername)
//                .password(decryptedDatabasePassword)
//                .driverClassName("com.mysql.cj.jdbc.Driver");
//
//        return dataSourceBuilder.build();
//    }
//}
