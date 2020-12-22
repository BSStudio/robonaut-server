package hu.bsstudio.race.skill.timer;

import hu.bsstudio.race.skill.timer.model.SkillTimer;
import reactor.core.publisher.Mono;

public interface SkillTimerService {
    Mono<SkillTimer> startTimer(final SkillTimer skillTimer);

    Mono<SkillTimer> stopTimerAt(final SkillTimer skillTimer);
}
