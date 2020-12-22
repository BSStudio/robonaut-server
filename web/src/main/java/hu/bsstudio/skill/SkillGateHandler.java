package hu.bsstudio.skill;

import hu.bsstudio.race.skill.SkillRaceService;
import hu.bsstudio.race.skill.model.GateInfo;
import hu.bsstudio.team.model.DetailedTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SkillGateHandler implements HandlerFunction<ServerResponse> {

    private final SkillRaceService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToMono(GateInfo.class)
            .flatMap(service::updateSkillRaceResultOnGate);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
