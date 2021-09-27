package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class AdminUpdateTeamHandler(private val teamService: TeamService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux<DetailedTeam>()
            .flatMap(teamService::updateTeam)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
