package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import reactor.core.publisher.Mono;

public interface SkillTimerService {
    Mono<SkillTimer> updateTimer(final SkillTimer skillTimer);
}
