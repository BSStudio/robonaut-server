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

    override fun addTeam(team: Team): Mono<DetailedTeam> {
        return service.addTeam(team)
            .doOnNext { detailedTeam: DetailedTeam -> sendTeamInfo(detailedTeam) }
    }

    override fun updateTeam(team: Team): Mono<DetailedTeam> {
        return service.updateTeam(team)
            .doOnNext(::sendTeamInfo)
    }

    override fun updateTeam(detailedTeam: DetailedTeam): Mono<DetailedTeam> {
        return service.updateTeam(detailedTeam)
            .doOnNext(::sendTeamInfo)
    }

    override fun findAllTeam(): Flux<DetailedTeam> {
        return service.findAllTeam()
            .doOnNext { detailedTeam: DetailedTeam -> sendTeamInfo(detailedTeam) }
    }

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam)
    }

    companion object {
        const val TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
