package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Team
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class BroadcastingTeamService(
    private val template: RabbitTemplate,
    private val service: TeamService,
) : TeamService {
    override fun addTeam(team: Team): Mono<DetailedTeam> =
        service
            .addTeam(team)
            .doOnNext(::sendTeamInfo)

    override fun updateTeam(team: Team): Mono<DetailedTeam> =
        service
            .updateTeam(team)
            .doOnNext(::sendTeamInfo)

    override fun updateTeam(detailedTeam: DetailedTeam): Mono<DetailedTeam> =
        service
            .updateTeam(detailedTeam)
            .doOnNext(::sendTeamInfo)

    override fun findAllTeam(): Flux<DetailedTeam> =
        service
            .findAllTeam()
            .doOnNext(::sendTeamInfo)

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam)
    }

    companion object {
        const val TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
