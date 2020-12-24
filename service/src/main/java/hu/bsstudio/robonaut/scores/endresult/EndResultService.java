package hu.bsstudio.robonaut.scores.endresult;

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface EndResultService {
    Mono<DetailedTeam> updateEndResult(final EndResultedTeam endResultedTeam);
}
