package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSkillTimerService implements SkillTimerService {

    @Override
    public Mono<SkillTimer> startTimer(final SkillTimer skillTimer) {
        return Mono.just(skillTimer)
            .filter(this::timerActionIsStart);
    }

    @Override
    public Mono<SkillTimer> stopTimerAt(final SkillTimer skillTimer) {
        return Mono.just(skillTimer)
            .filter(this::timerActionIsStop);
    }

    private boolean timerActionIsStart(final SkillTimer timer) {
        return timer.getTimerAction().equals(TimerAction.START);
    }

    private boolean timerActionIsStop(final SkillTimer timer) {
        return timer.getTimerAction().equals(TimerAction.STOP);
    }
}
