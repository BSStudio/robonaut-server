package hu.bsstudio.team;

import hu.bsstudio.team.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateTeamHandler implements HandlerFunction<ServerResponse> {

    private final TeamService teamService;

    @Override
    public Mono<ServerResponse> handle(final ServerRequest request) {
        return request.bodyToMono(Team.class)
            .flatMap(teamService::updateTeam)
            .flatMap(team -> ServerResponse.ok().body(BodyInserters.fromValue(team)));
    }
}
