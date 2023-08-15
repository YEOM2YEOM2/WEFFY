package openvidu.meeting.service.java.common.config;

import openvidu.meeting.service.java.common.kms.KmsService;
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

    public DataSourceConfiguration(KmsService kmsService,
                                   @Value("${spring.datasource.url}") String url,
                                   @Value("${spring.datasource.username}") String username,
                                   @Value("${spring.datasource.password}") String password) {
        this.kmsService = kmsService;
        this.url = url;
        this.username = username;
        this.password = password;
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
                .password(decryptedDatabasePassword)
                .driverClassName("com.mysql.cj.jdbc.Driver");

        return dataSourceBuilder.build();
    }
}
