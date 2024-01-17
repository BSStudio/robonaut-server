package hu.bsstudio.robonaut.configuration

import hu.bsstudio.robonaut.security.RobonAuthFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration(
    @Value("\${robonauth.api-key}") private val apiKey: String,
) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.httpBasic { it.disable() }
            .cors { it.disable() }
            .csrf { it.disable() }
            .build()
    }

    @Bean
    fun robonAuthFilter() = RobonAuthFilter(apiKey)
}
