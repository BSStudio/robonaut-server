package hu.bsstudio.race.speed;

import hu.bsstudio.race.speed.model.SpeedTimer;
import hu.bsstudio.race.speed.model.TimerAction;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSpeedRaceService implements SpeedRaceService {

    private static final int TIMER_INITIAL_VALUE = 0;

    @Override
    public Mono<SpeedTimer> startTimer() {
        return Mono.just(new SpeedTimer(TIMER_INITIAL_VALUE, TimerAction.START));
        // todo broadcast timer started
    }

    @Override
    public Mono<SpeedTimer> stopTimerAt(final SpeedTimer speedTimer) {
        return Mono.just(speedTimer)
            .filter(timer -> timer.getTimerAction().equals(TimerAction.STOP));
        // todo broadcast timer stopped at
    }
}
