package hu.bsstudio.robonaut.scores.qualification;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultQualificationScoreService implements QualificationScoreService {

    @NonNull
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
