package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import reactor.core.publisher.Mono;

public interface SkillTimerService {
    Mono<SkillTimer> startTimer(final SkillTimer skillTimer);

    Mono<SkillTimer> stopTimer(final SkillTimer skillTimer);
}
