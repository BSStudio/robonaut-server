package hu.bsstudio.robonaut.team

import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

class ReadAllTeamHandler(private val teamService: TeamService) : HandlerFunction<ServerResponse> {
    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return teamService.findAllTeam()
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
