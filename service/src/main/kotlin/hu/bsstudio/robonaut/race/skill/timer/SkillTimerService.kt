package hu.bsstudio.robonaut.race.skill.timer

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import reactor.core.publisher.Mono

interface SkillTimerService {
    fun updateTimer(skillTimer: SkillTimer): Mono<SkillTimer>
}
