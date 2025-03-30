package hu.bsstudio.robonaut.race.speed.timer.configuration

import hu.bsstudio.robonaut.race.speed.timer.BroadcastingSpeedTimerService
import hu.bsstudio.robonaut.race.speed.timer.DefaultSpeedTimerService
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpeedTimerServiceConfiguration(
    private val rabbitTemplate: RabbitTemplate,
) {
    @Bean
    fun speedTimerService(defaultSpeedTimerService: SpeedTimerService): SpeedTimerService =
        BroadcastingSpeedTimerService(rabbitTemplate, defaultSpeedTimerService)

    @Bean
    fun defaultSpeedTimerService(): SpeedTimerService = DefaultSpeedTimerService()
}
