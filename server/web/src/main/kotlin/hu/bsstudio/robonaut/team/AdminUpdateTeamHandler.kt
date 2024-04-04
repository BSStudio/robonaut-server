package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToFlux
import reactor.core.publisher.Mono

class AdminUpdateTeamHandler(private val teamService: TeamService) : HandlerFunction<ServerResponse> {
    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux<DetailedTeam>()
            .flatMap(teamService::updateTeam)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
