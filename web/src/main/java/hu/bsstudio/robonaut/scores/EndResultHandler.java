package hu.bsstudio.robonaut.scores;

import hu.bsstudio.robonaut.scores.endresult.EndResultService;
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EndResultHandler implements HandlerFunction<ServerResponse> {

    private final EndResultService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToFlux(EndResultedTeam.class)
            .flatMap(service::updateEndResult);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
