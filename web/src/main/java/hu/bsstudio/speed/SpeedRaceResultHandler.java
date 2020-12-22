package hu.bsstudio.speed;

import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.race.speed.model.SpeedRaceResult;
import hu.bsstudio.team.model.DetailedTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SpeedRaceResultHandler implements HandlerFunction<ServerResponse> {

    private final SpeedRaceService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToMono(SpeedRaceResult.class)
            .flatMap(service::updateSpeedRace);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
