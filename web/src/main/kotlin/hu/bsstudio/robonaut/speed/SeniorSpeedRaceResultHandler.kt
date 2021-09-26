package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class SeniorSpeedRaceResultHandler(private val service: SpeedRaceService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val detailedTeam = request.bodyToMono(SpeedRaceResult::class.java)
            .flatMap(service::updateSpeedRaceSenior)
        return ServerResponse.ok().body(detailedTeam, DetailedTeam::class.java)
    }
}
