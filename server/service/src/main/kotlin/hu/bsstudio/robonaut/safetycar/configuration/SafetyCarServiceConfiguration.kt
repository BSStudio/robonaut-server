package hu.bsstudio.robonaut.safetycar.configuration

import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.safetycar.BroadcastingSafetyCarService
import hu.bsstudio.robonaut.safetycar.DefaultSafetyCarService
import hu.bsstudio.robonaut.safetycar.SafetyCarService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SafetyCarServiceConfiguration(
    private val repository: TeamRepository,
    private val rabbitTemplate: RabbitTemplate,
) {
    @Bean
    fun safetyCarService(defaultSafetyCarService: SafetyCarService): SafetyCarService {
        return BroadcastingSafetyCarService(rabbitTemplate, defaultSafetyCarService)
    }

    @Bean
    fun defaultSafetyCarService(): SafetyCarService {
        return DefaultSafetyCarService(repository)
    }
}
