package hu.bsstudio.robonaut.speed;

import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService;
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateSpeedTimerHandler implements HandlerFunction<ServerResponse> {

    @NonNull
    private final SpeedTimerService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var speedTimer = request.bodyToMono(SpeedTimer.class)
            .flatMap(service::updateTimer);
        return ServerResponse.ok().body(speedTimer, SpeedTimer.class);
    }
}
