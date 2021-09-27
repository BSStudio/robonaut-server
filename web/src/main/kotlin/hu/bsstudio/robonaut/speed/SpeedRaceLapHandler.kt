package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class SpeedRaceLapHandler(private val service: SpeedRaceService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono< SpeedRaceScore>()
            .flatMap(service::updateSpeedRaceOnLap)
            .let { detailedTeam -> ServerResponse.ok().body(detailedTeam) }
    }
}
