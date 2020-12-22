package hu.bsstudio.scores.audience;

import hu.bsstudio.scores.audience.model.AudienceScoredTeam;
import hu.bsstudio.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface AudienceScoreService {
    Mono<DetailedTeam> updateAudienceScore(final AudienceScoredTeam audienceScoredTeam);
}
