package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class AdminUpdateTeamHandler(private val teamService: TeamService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToFlux(DetailedTeam::class.java)
            .flatMap { team: DetailedTeam? -> teamService.updateTeam(team) }
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
