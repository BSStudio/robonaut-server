package hu.bsstudio.speed;

import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.race.speed.model.SpeedTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StopTimerHandler implements HandlerFunction<ServerResponse> {

    private final SpeedRaceService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        return request.bodyToMono(SpeedTimer.class)
            .flatMap(service::stopTimerAt)
            .flatMap(speedTimer -> ServerResponse.ok().body(BodyInserters.fromValue(speedTimer)));
    }
}
