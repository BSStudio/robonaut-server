package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class SeniorSpeedRaceResultHandler(
  private val service: SpeedRaceService,
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono<SpeedRaceResult>()
      .flatMap(service::updateSpeedRaceSenior)
      .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
}
