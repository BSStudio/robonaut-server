package hu.bsstudio.robonaut.scores.qualification.configuration

import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.qualification.BroadcastingQualificationScoreService
import hu.bsstudio.robonaut.scores.qualification.DefaultQualificationScoreService
import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QualificationScoreServiceConfiguration(
    private val rabbitTemplate: RabbitTemplate,
    private val repository: TeamRepository,
) {
    @Bean
    fun qualificationScoreService(defaultQualificationScoreService: QualificationScoreService): QualificationScoreService =
        BroadcastingQualificationScoreService(rabbitTemplate, defaultQualificationScoreService)

    @Bean
    fun defaultQualificationScoreService(): QualificationScoreService = DefaultQualificationScoreService(repository)
}
