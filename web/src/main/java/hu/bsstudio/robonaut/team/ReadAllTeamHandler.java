package hu.bsstudio.robonaut.team;

import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReadAllTeamHandler implements HandlerFunction<ServerResponse> {

    @NonNull
    private final TeamService teamService;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = teamService.findAllTeam();
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
