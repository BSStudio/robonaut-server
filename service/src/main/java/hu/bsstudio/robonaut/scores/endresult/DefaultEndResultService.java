package hu.bsstudio.robonaut.scores.endresult;

import hu.bsstudio.robonaut.entity.ScoreEntity;
import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.entity.TeamType;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultEndResultService implements EndResultService {

    @NonNull
    private final TeamRepository teamRepository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper teamModelEntityMapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> updateEndResultSenior(final EndResultedTeam endResultedTeam) {
        return Mono.just(endResultedTeam)
            .map(EndResultedTeam::getTeamId)
            .flatMap(teamRepository::findById)
            .map(entity -> addEndResultSenior(entity, endResultedTeam))
            .flatMap(teamRepository::save)
            .map(teamModelEntityMapper::toModel);
    }

    @Override
    public Mono<DetailedTeam> updateEndResultJunior(final EndResultedTeam endResultedTeam) {
        return Mono.just(endResultedTeam)
            .map(EndResultedTeam::getTeamId)
            .flatMap(teamRepository::findById)
            .filter(teamEntity -> teamEntity.getTeamType() == TeamType.JUNIOR)
            .map(entity -> addEndResultJunior(entity, endResultedTeam))
            .flatMap(teamRepository::save)
            .map(teamModelEntityMapper::toModel);
    }

    private TeamEntity addEndResultSenior(final TeamEntity entity, final EndResultedTeam endResultedTeam) {
        final var score = updateTotalScore(endResultedTeam, entity.getScore());
        entity.setScore(score);
        return entity;
    }

    private TeamEntity addEndResultJunior(final TeamEntity entity, final EndResultedTeam endResultedTeam) {
        final var juniorScore = updateTotalScore(endResultedTeam, entity.getJuniorScore());
        entity.setJuniorScore(juniorScore);
        return entity;
    }

    private ScoreEntity updateTotalScore(final EndResultedTeam endResultedTeam, final ScoreEntity score) {
        score.setScore(endResultedTeam.getTotalScore());
        return score;
    }
}
