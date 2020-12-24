package hu.bsstudio.robonaut.scores.audience;

import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface AudienceScoreService {
    Mono<DetailedTeam> updateAudienceScore(final AudienceScoredTeam audienceScoredTeam);
}
