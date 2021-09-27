package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

class UpdateSpeedTimerHandler(private val service: SpeedTimerService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono<SpeedTimer>()
            .flatMap(service::updateTimer)
            .let { speedTimer -> ServerResponse.ok().body(speedTimer) }
    }
}
