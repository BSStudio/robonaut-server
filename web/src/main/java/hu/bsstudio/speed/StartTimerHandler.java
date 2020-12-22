package hu.bsstudio.speed;

import hu.bsstudio.race.speed.SpeedRaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StartTimerHandler implements HandlerFunction<ServerResponse> {

    private final SpeedRaceService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        return service.startTimer()
            .flatMap(startTimer -> ServerResponse.ok().body(BodyInserters.fromValue(startTimer)));
    }
}
