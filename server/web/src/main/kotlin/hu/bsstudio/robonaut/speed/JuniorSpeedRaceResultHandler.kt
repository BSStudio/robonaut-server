package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class JuniorSpeedRaceResultHandler(
  private val service: SpeedRaceService,
) : HandlerFunction<ServerResponse> {
  override fun handle(request: ServerRequest): Mono<ServerResponse> =
    request
      .bodyToMono<SpeedRaceResult>()
      .flatMap(service::updateSpeedRaceJunior)
      .doOnError { error -> LOG.error("Junior Speed Race error: ", error) }
      .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }


  companion object {
    private val LOG = LoggerFactory.getLogger(this::class.java)
  }
}
