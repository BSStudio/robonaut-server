package hu.bsstudio.robonaut.race.speed.timer

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import reactor.core.publisher.Mono

interface SpeedTimerService {
    fun updateTimer(speedTimer: SpeedTimer): Mono<SpeedTimer>
}
