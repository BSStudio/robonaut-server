package hu.bsstudio.configuration;

import hu.bsstudio.security.RobonAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Value("${robonauth.api-key}")
    private String apiKey;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http.httpBasic().disable()
            .cors().disable()
            .csrf().disable()
            .build();
    }

    @Bean
    public RobonAuthFilter robonAuthFilter() {
        return new RobonAuthFilter(apiKey);
    }
}
