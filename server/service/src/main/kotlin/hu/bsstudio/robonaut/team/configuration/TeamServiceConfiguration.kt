package hu.bsstudio.robonaut.team.configuration

import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.team.BroadcastingTeamService
import hu.bsstudio.robonaut.team.DefaultTeamService
import hu.bsstudio.robonaut.team.TeamService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TeamServiceConfiguration(
    private val rabbitTemplate: RabbitTemplate,
    private val teamRepository: TeamRepository,
) {
    @Bean
    fun teamService(defaultTeamService: TeamService): TeamService {
        return BroadcastingTeamService(rabbitTemplate, defaultTeamService)
    }

    @Bean
    fun defaultTeamService(): TeamService = DefaultTeamService(teamRepository)
}
