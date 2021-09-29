package hu.bsstudio.robonaut.race.skill.timer.configuration

import hu.bsstudio.robonaut.race.skill.timer.BroadcastingSkillTimerService
import hu.bsstudio.robonaut.race.skill.timer.DefaultSkillTimerService
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SkillTimerServiceConfiguration(
    @Autowired private val rabbitTemplate: RabbitTemplate
) {

    @Bean
    fun skillTimerService(defaultSkillTimerService: SkillTimerService): SkillTimerService {
        return BroadcastingSkillTimerService(rabbitTemplate, defaultSkillTimerService)
    }

    @Bean
    fun defaultSkillTimerService(): SkillTimerService = DefaultSkillTimerService()
}
