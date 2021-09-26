package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSpeedTimerService implements SpeedTimerService {

    @Override
    public Mono<SpeedTimer> updateTimer(final SpeedTimer speedTimer) {
        return Mono.just(speedTimer);
    }
}
