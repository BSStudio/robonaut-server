package hu.bsstudio.robonaut.race.speed.timer

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import reactor.core.publisher.Mono

class DefaultSpeedTimerService : SpeedTimerService {
    override fun updateTimer(speedTimer: SpeedTimer): Mono<SpeedTimer> {
        return Mono.just(speedTimer)
    }
}
