package hu.bsstudio.robonaut.configuration

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration {
    @Bean
    fun generalTeamDataQueue() = Queue("general.teamData")

    @Bean
    fun startSkillTimerQueue() = Queue("skill.timer")

    @Bean
    fun skillRaceGateQueue() = Queue("skill.gate")

    @Bean
    fun safetyCarFollowQueue() = Queue("speed.safetyCar.follow")

    @Bean
    fun safetyCarOvertakeQueue() = Queue("speed.safetyCar.overtake")

    @Bean
    fun startSpeedTimerQueue() = Queue("speed.timer")

    @Bean
    fun speedRaceLapQueue() = Queue("speed.lap")

    @Bean
    fun teamDataQueue() = Queue("team.teamData")

    @Bean
    fun jackson2JsonMessageConverter() = Jackson2JsonMessageConverter()

    @Bean
    fun connectionNameStrategy(): ConnectionNameStrategy {
        return SimplePropertyValueConnectionNameStrategy("spring.application.name")
    }
}
