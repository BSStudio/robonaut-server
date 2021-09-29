package hu.bsstudio.robonaut.race.skill.timer

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingSkillTimerService(
    private val template: RabbitTemplate,
    private val service: SkillTimerService
) : SkillTimerService {

    override fun updateTimer(skillTimer: SkillTimer): Mono<SkillTimer> {
        return service.updateTimer(skillTimer)
            .doOnNext(::sendSkillTimerData)
    }

    private fun sendSkillTimerData(timer: SkillTimer) {
        template.convertAndSend("skill.timer", timer)
    }
}
