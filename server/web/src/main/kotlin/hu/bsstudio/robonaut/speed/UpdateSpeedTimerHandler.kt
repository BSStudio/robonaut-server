package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

class UpdateSpeedTimerHandler(
    private val service: SpeedTimerService,
) : HandlerFunction<ServerResponse> {
    override fun handle(request: ServerRequest): Mono<ServerResponse> =
        request
            .bodyToMono<SpeedTimer>()
            .flatMap(service::updateTimer)
            .let { speedTimer -> ServerResponse.ok().body(speedTimer) }
}
