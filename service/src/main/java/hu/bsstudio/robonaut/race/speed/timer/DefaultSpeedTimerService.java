package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSpeedTimerService implements SpeedTimerService {

    private static final int TIMER_INITIAL_VALUE = 0;

    @Override
    public Mono<SpeedTimer> startTimer() {
        return Mono.just(new SpeedTimer(TIMER_INITIAL_VALUE, TimerAction.START));
    }

    @Override
    public Mono<SpeedTimer> stopTimerAt(final SpeedTimer speedTimer) {
        return Mono.just(speedTimer)
            .filter(timer -> timer.getTimerAction().equals(TimerAction.STOP));
    }
}
