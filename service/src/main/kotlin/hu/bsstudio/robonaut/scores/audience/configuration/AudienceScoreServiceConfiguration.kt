package hu.bsstudio.robonaut.scores.audience.configuration

import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.audience.AudienceScoreService
import hu.bsstudio.robonaut.scores.audience.BroadcastingAudienceScoreService
import hu.bsstudio.robonaut.scores.audience.DefaultAudienceScoreService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AudienceScoreServiceConfiguration(
    @Autowired private val rabbitTemplate: RabbitTemplate,
    @Autowired private val teamRepository: TeamRepository,
) {

    @Bean
    fun audienceScoreService(defaultAudienceScoreService: AudienceScoreService): AudienceScoreService {
        return BroadcastingAudienceScoreService(rabbitTemplate, defaultAudienceScoreService)
    }

    @Bean
    fun defaultAudienceScoreService(): AudienceScoreService = DefaultAudienceScoreService(teamRepository)
}
