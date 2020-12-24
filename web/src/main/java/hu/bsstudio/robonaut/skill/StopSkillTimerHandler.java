package hu.bsstudio.robonaut.skill;

import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService;
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class StopSkillTimerHandler implements HandlerFunction<ServerResponse> {

    private final SkillTimerService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var skillTimer = request.bodyToMono(SkillTimer.class)
            .flatMap(service::stopTimerAt);
        return ServerResponse.ok().body(skillTimer, SkillTimer.class);
    }
}
