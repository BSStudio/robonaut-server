package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSkillTimerService implements SkillTimerService {

    @Override
    public Mono<SkillTimer> updateTimer(final SkillTimer skillTimer) {
        return Mono.just(skillTimer);
    }
}
