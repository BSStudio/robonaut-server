package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.Team
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class UpdateTeamHandler(private val teamService: TeamService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<Team>()
            .flatMap(teamService::updateTeam)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
