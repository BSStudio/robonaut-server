package hu.bsstudio.robonaut.race.skill.timer

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import reactor.core.publisher.Mono

class DefaultSkillTimerService : SkillTimerService {
  override fun updateTimer(skillTimer: SkillTimer): Mono<SkillTimer> = Mono.just(skillTimer)
}
