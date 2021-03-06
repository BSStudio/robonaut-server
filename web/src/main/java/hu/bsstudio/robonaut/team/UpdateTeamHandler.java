package hu.bsstudio.robonaut.team;

import hu.bsstudio.robonaut.team.model.DetailedTeam;
import hu.bsstudio.robonaut.team.model.Team;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateTeamHandler implements HandlerFunction<ServerResponse> {

    @NonNull
    private final TeamService teamService;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        final var detailedTeam = request.bodyToMono(Team.class)
            .flatMap(teamService::updateTeam);
        return ServerResponse.ok().body(detailedTeam, DetailedTeam.class);
    }
}
