package hu.bsstudio.robonaut.race.speed.configuration

import hu.bsstudio.robonaut.race.speed.BroadcastingSpeedRaceService
import hu.bsstudio.robonaut.race.speed.DefaultSpeedRaceService
import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.repository.TeamRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpeedRaceServiceConfiguration(
    private val repository: TeamRepository,
    private val rabbitTemplate: RabbitTemplate,
) {
    @Bean
    fun speedRaceService(defaultSpeedRaceService: SpeedRaceService): SpeedRaceService =
        BroadcastingSpeedRaceService(rabbitTemplate, defaultSpeedRaceService)

    @Bean
    fun defaultSpeedRaceService(): SpeedRaceService = DefaultSpeedRaceService(repository)
}
