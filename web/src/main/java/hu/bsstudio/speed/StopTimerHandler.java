package hu.bsstudio.speed;

import hu.bsstudio.race.speed.timer.SpeedTimerService;
import hu.bsstudio.race.speed.timer.model.SpeedTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StopTimerHandler implements HandlerFunction<ServerResponse> {

    private final SpeedTimerService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var speedTimer = request.bodyToMono(SpeedTimer.class)
            .flatMap(service::stopTimerAt);
        return ServerResponse.ok().body(speedTimer, SpeedTimer.class);
    }
}
