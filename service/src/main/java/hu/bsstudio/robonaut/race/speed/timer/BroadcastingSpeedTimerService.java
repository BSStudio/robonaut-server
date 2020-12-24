package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSpeedTimerService implements SpeedTimerService {

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SpeedTimerService service;

    @Override
    public Mono<SpeedTimer> startTimer() {
        return service.startTimer()
            .doOnNext(this::sendTimerStarted);
    }

    @Override
    public Mono<SpeedTimer> stopTimerAt(final SpeedTimer speedTimer) {
        return service.stopTimerAt(speedTimer)
            .doOnNext(this::sendTimerStopped);
    }

    private void sendTimerStarted(final SpeedTimer timer) {
        template.convertAndSend("speed.startTimer", timer);
    }

    private void sendTimerStopped(final SpeedTimer timer) {
        template.convertAndSend("speed.stopTimer", timer);
    }
}
