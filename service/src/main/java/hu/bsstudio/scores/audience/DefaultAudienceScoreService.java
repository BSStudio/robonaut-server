package hu.bsstudio.scores.audience;

import hu.bsstudio.entity.TeamEntity;
import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.scores.audience.model.AudienceScoredTeam;
import hu.bsstudio.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultAudienceScoreService implements AudienceScoreService {

    private final TeamRepository teamRepository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper teamModelEntityMapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> updateAudienceScore(final AudienceScoredTeam audienceScoredTeam) {
        return Mono.just(audienceScoredTeam)
            .map(AudienceScoredTeam::getTeamId)
            .flatMap(teamRepository::findById)
            .map(entity -> addAudienceScore(entity, audienceScoredTeam))
            .flatMap(teamRepository::save)
            .map(teamModelEntityMapper::toModel);
    }

    private TeamEntity addAudienceScore(final TeamEntity entity, final AudienceScoredTeam audienceScoredTeam) {
        entity.setVotes(audienceScoredTeam.getVotes());
        entity.setAudienceScore(audienceScoredTeam.getAudienceScore());
        return entity;
    }
}
