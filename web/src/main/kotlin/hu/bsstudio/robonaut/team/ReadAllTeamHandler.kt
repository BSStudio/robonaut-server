package hu.bsstudio.robonaut.team

import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerResponse
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono

class ReadAllTeamHandler(private val teamService: TeamService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = teamService.findAllTeam()
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
