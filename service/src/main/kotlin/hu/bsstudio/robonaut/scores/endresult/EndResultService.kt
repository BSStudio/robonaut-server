package hu.bsstudio.robonaut.scores.endresult;

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface EndResultService {
    Mono<DetailedTeam> updateEndResultSenior(final EndResultedTeam endResultedTeam);

    Mono<DetailedTeam> updateEndResultJunior(final EndResultedTeam endResultedTeam);
}
