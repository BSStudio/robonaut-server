package hu.bsstudio.robonaut.race.skill.configuration

import hu.bsstudio.robonaut.race.skill.BroadcastingSkillRaceService
import hu.bsstudio.robonaut.race.skill.DefaultSkillRaceService
import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.repository.TeamRepository
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SkillRaceServiceConfiguration(
  private val rabbitTemplate: RabbitTemplate,
  private val repository: TeamRepository,
) {
  @Bean
  fun skillRaceService(defaultSkillRaceService: SkillRaceService): SkillRaceService =
    BroadcastingSkillRaceService(rabbitTemplate, defaultSkillRaceService)

  @Bean
  fun defaultSkillRaceService(): SkillRaceService = DefaultSkillRaceService(repository)
}
