package hu.bsstudio.scores.qualification;

import hu.bsstudio.scores.qualification.model.QualifiedTeam;
import hu.bsstudio.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface QualificationScoreService {
    Mono<DetailedTeam> updateQualificationScore(final QualifiedTeam team);
}
