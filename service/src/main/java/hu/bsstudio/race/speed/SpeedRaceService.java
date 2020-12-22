package hu.bsstudio.race.speed;

import hu.bsstudio.race.speed.model.SpeedTimer;
import hu.bsstudio.race.speed.model.TimerAction;
import reactor.core.publisher.Mono;

public interface SpeedRaceService {
    Mono<SpeedTimer> startTimer();
    Mono<SpeedTimer> stopTimerAt(final SpeedTimer speedTimer);
}
