package hu.bsstudio.robonaut.scores;

import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService;
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class QualificationScoreHandler implements HandlerFunction<ServerResponse> {

    @NonNull
    private final QualificationScoreService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToFlux(QualifiedTeam.class)
            .flatMap(service::updateQualificationScore);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
