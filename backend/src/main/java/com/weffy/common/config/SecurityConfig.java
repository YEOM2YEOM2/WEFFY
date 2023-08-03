package com.weffy.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 토큰을 활성화, CSRF 토큰의 생성, 저장, 검증 등은 Spring Security가 자동으로 처리
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/users/signin")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // mysql 데이터베이스 콘솔, 정적 리소스, swagger 경로 인증 권한 설정
                        .requestMatchers("/api/v1/users/signin", "/signup", "/mysql-console/**", "/static/**", "/swagger-ui/**", "/api-docs/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .disable())
//                        .loginPage("/signin")
//                        .defaultSuccessUrl("/")
//                        .failureUrl("/signin")
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                        .loginProcessingUrl("/signin")
//                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/signin")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe
                        .key("rememberMe")
                        .rememberMeParameter("remember")
                        .tokenValiditySeconds(3600)
                        .alwaysRemember(true)
//                        .tokenRepository(persistentTokenRepository())
//                        .rememberMeServices(rememberMeServices(persistentTokenRepository()))
//                        .userDetailsService(new UserDetailsServiceImpl(memberRepository)))
                );
        return http.build();
    }

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
//        repo.setDataSource(dataSource);
//        return repo;
//    }

//    @Bean
//    public PersistentTokenBasedRememberMeServices rememberMeServices(PersistentTokenRepository tokenRepository) {
//        PersistentTokenBasedRememberMeServices rememberMeServices = new
//                PersistentTokenBasedRememberMeServices("rememberMeKey", new UserDetailsServiceImpl(memberRepository), tokenRepository);
//        rememberMeServices.setParameter("remember-me");
//        rememberMeServices.setAlwaysRemember(true);
//        return rememberMeServices;
//    }

    // password encoder로 사용할 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder defaultEncoder = new BCryptPasswordEncoder();
        String idForEncode = "bcrypt";

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, defaultEncoder);

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }


    //CORS 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}