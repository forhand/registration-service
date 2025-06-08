package com.example.registrationservice.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12); // Используем силу хэширования 12 (по умолчанию 10)
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // Отключаем CSRF для API
        .cors(withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/v1/registration/**")
                    .permitAll() // Разрешаем доступ к эндпоинту регистрации
                    .requestMatchers("/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated() // Все остальные запросы требуют аутентификации
            )
        .httpBasic(withDefaults());

    return http.build();
  }
}
