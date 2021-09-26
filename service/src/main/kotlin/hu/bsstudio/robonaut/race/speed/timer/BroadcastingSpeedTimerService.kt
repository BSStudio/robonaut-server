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
            .doOnNext(this::sendTimerInformation)
    }

    private fun sendTimerInformation(timer: SpeedTimer) {
        template.convertAndSend(SPEED_TIMER_ROUTING_KEY, timer)
    }

    companion object {
        private const val SPEED_TIMER_ROUTING_KEY = "speed.timer"
    }
}