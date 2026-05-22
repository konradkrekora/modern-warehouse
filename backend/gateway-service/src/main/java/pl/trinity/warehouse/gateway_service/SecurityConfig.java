package pl.trinity.warehouse.gateway_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // W architekturze z tokenami JWT bez CSRF, bo tokeny same w sobie chronią przed tym atakiem
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Adresy logowania i rejestracji pracowników będą publiczne
                        .pathMatchers("/auth/**").permitAll()
                        // Każde inne żądanie do mikroserwisów WYMAGA zalogowania
                        .anyExchange().authenticated()
                )
                // Na razie bez domyślnych formularzy logowania Springa, pozniej własny mechanizm JWT
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .build();
    }
}
