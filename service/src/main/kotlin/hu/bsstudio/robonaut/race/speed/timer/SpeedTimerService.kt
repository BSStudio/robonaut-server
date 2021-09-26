package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import reactor.core.publisher.Mono;

public interface SpeedTimerService {
    Mono<SpeedTimer> updateTimer(final SpeedTimer speedTimer);
}
