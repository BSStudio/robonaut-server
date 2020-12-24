package hu.bsstudio.robonaut.team;

import hu.bsstudio.robonaut.team.model.DetailedTeam;
import hu.bsstudio.robonaut.team.model.Team;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeamService {
    Mono<DetailedTeam> addTeam(final Team team);

    Mono<DetailedTeam> updateTeam(final Team team);

    Flux<DetailedTeam> findAllTeam();
}
