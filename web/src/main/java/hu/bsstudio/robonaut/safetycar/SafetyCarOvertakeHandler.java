package hu.bsstudio.robonaut.safetycar;

import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SafetyCarOvertakeHandler implements HandlerFunction<ServerResponse> {

    @NonNull
    private final SafetyCarService service;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToMono(SafetyCarOvertakeInformation.class)
            .flatMap(service::safetyCarWasOvertaken);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
