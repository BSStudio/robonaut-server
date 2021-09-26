package hu.bsstudio.robonaut.safetycar.configuration

import org.springframework.beans.factory.annotation.Autowired
import hu.bsstudio.robonaut.repository.TeamRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import hu.bsstudio.robonaut.safetycar.SafetyCarService
import hu.bsstudio.robonaut.safetycar.BroadcastingSafetyCarService
import hu.bsstudio.robonaut.safetycar.DefaultSafetyCarService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SafetyCarServiceConfiguration(
    @Autowired private val repository: TeamRepository,
    @Autowired private val rabbitTemplate: RabbitTemplate,
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
