package hu.bsstudio.speed;

import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.race.speed.model.SpeedRaceScore;
import hu.bsstudio.team.model.DetailedTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SpeedRaceLapHandler implements HandlerFunction<ServerResponse> {

    private final SpeedRaceService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToMono(SpeedRaceScore.class)
            .flatMap(service::updateSpeedRaceOnLap);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
