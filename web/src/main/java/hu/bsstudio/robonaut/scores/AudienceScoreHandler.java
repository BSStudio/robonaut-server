package hu.bsstudio.robonaut.scores;

import hu.bsstudio.robonaut.scores.audience.AudienceScoreService;
import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AudienceScoreHandler implements HandlerFunction<ServerResponse> {

    private final AudienceScoreService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToFlux(AudienceScoredTeam.class)
            .flatMap(service::updateAudienceScore);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
