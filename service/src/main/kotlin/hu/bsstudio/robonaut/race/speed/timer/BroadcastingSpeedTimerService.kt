package hu.bsstudio.robonaut.race.speed.timer

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingSpeedTimerService(
    private val template: RabbitTemplate,
    private val service: SpeedTimerService,
) : SpeedTimerService {

    override fun updateTimer(speedTimer: SpeedTimer): Mono<SpeedTimer> {
        return service.updateTimer(speedTimer)
            .doOnNext(::sendTimerInformation)
    }

    private fun sendTimerInformation(timer: SpeedTimer) {
        template.convertAndSend("speed.timer", timer)
    }
}
