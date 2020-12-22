package hu.bsstudio.scores.qualification;

import hu.bsstudio.entity.TeamEntity;
import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.scores.qualification.model.QualifiedTeam;
import hu.bsstudio.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultQualificationScoreService implements QualificationScoreService {

    private final TeamRepository teamRepository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper mapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> updateQualificationScore(final QualifiedTeam qualifiedTeam) {
        return Mono.just(qualifiedTeam)
            .map(QualifiedTeam::getTeamId)
            .flatMap(teamRepository::findById)
            .map(entity -> addQualificationScore(entity, qualifiedTeam))
            .flatMap(teamRepository::save)
            .map(mapper::toModel);
    }

    private TeamEntity addQualificationScore(final TeamEntity entity, final QualifiedTeam qualifiedTeam) {
        entity.setQualificationScore(qualifiedTeam.getQualificationScore());
        return entity;
    }
}
