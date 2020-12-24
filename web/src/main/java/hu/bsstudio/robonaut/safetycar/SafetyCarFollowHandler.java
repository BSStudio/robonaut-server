package hu.bsstudio.robonaut.safetycar;

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SafetyCarFollowHandler implements HandlerFunction<ServerResponse> {

    private final SafetyCarService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var safety = request.bodyToMono(SafetyCarFollowInformation.class)
            .flatMap(service::safetyCarWasFollowed);
        return ServerResponse.ok().body(safety, DetailedTeam.class);
    }
}
