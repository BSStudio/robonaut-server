package hu.bsstudio.robonaut.scores.audience;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultAudienceScoreService implements AudienceScoreService {

    @NonNull
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
