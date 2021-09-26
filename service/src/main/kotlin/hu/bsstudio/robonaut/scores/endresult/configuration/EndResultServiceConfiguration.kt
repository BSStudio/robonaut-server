package hu.bsstudio.robonaut.scores.endresult.configuration

import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.endresult.BroadcastingEndResultService
import hu.bsstudio.robonaut.scores.endresult.DefaultEndResultService
import hu.bsstudio.robonaut.scores.endresult.EndResultService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EndResultServiceConfiguration(
    @Autowired private val rabbitTemplate: RabbitTemplate,
    @Autowired private val teamRepository: TeamRepository
) {

    @Bean
    fun endResultService(defaultEndResultService: EndResultService): EndResultService {
        return BroadcastingEndResultService(rabbitTemplate, defaultEndResultService)
    }

    @Bean
    fun defaultEndResultService(): EndResultService = DefaultEndResultService(teamRepository)
}
