package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class UpdateSpeedTimerHandler(private val service: SpeedTimerService) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val speedTimer = request.bodyToMono(SpeedTimer::class.java)
            .flatMap { speedTimer: SpeedTimer? -> service.updateTimer(speedTimer) }
        return ServerResponse.ok().body(speedTimer, SpeedTimer::class.java)
    }
}
