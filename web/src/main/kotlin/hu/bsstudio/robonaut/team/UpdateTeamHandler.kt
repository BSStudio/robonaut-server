package hu.bsstudio.robonaut.team

import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerResponse
import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Team
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

class UpdateTeamHandler(private val teamService: TeamService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToMono(Team::class.java)
            .flatMap { team: Team? -> teamService.updateTeam(team) }
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
