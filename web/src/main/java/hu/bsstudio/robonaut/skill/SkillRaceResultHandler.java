package hu.bsstudio.robonaut.skill;

import hu.bsstudio.robonaut.race.skill.SkillRaceService;
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SkillRaceResultHandler implements HandlerFunction<ServerResponse> {

    @NonNull
    private final SkillRaceService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToMono(SkillRaceResult.class)
            .flatMap(service::updateSkillRaceResult);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
