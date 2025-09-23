package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.Team
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class CreateTeamHandler(
  private val teamService: TeamService,
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono<Team>()
      .flatMap(teamService::addTeam)
      .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
}
