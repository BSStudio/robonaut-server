package hu.bsstudio.scores.endresult;

import hu.bsstudio.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface EndResultService {
    Mono<DetailedTeam> updateEndResult(final EndResultedTeam endResultedTeam);
}
