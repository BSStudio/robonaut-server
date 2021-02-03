package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BroadcastingSpeedTimerService implements SpeedTimerService {

    public static final String SPEED_TIMER_ROUTING_KEY = "speed.timer";

    @NonNull
    private final RabbitTemplate template;
    @NonNull
    private final SpeedTimerService service;

    @Override
    public Mono<SpeedTimer> updateTimer(final SpeedTimer speedTimer) {
        return service.updateTimer(speedTimer)
            .doOnNext(this::sendTimerInformation);
    }

    private void sendTimerInformation(final SpeedTimer timer) {
        template.convertAndSend(SPEED_TIMER_ROUTING_KEY, timer);
    }
}
