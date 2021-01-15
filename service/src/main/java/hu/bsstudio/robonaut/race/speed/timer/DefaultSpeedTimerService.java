package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSpeedTimerService implements SpeedTimerService {

    @Override
    public Mono<SpeedTimer> startTimer(final SpeedTimer speedTimer) {
        return Mono.just(speedTimer)
            .filter(timer -> timer.getTimerAction() == TimerAction.START);
    }

    @Override
    public Mono<SpeedTimer> stopTimerAt(final SpeedTimer speedTimer) {
        return Mono.just(speedTimer)
            .filter(timer -> timer.getTimerAction() == TimerAction.STOP);
    }
}
