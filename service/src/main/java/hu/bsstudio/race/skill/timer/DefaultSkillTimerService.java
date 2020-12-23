package hu.bsstudio.race.skill.timer;

import hu.bsstudio.common.model.TimerAction;
import hu.bsstudio.race.skill.timer.model.SkillTimer;
import reactor.core.publisher.Mono;

public class DefaultSkillTimerService implements SkillTimerService {

    @Override
    public Mono<SkillTimer> startTimer(final SkillTimer skillTimer) {
        return Mono.just(skillTimer)
            .filter(timer -> timer.getTimerAction().equals(TimerAction.START));
        // todo broadcast timer
    }

    @Override
    public Mono<SkillTimer> stopTimerAt(final SkillTimer skillTimer) {
        return Mono.just(skillTimer)
            .filter(timer -> timer.getTimerAction().equals(TimerAction.STOP));
        // todo broadcast timer
    }
}
