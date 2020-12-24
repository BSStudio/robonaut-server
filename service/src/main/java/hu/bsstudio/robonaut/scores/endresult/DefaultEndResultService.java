package hu.bsstudio.robonaut.scores.endresult;

import hu.bsstudio.robonaut.entity.TeamEntity;
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
    public Mono<DetailedTeam> updateEndResult(final EndResultedTeam endResultedTeam) {
        return Mono.just(endResultedTeam)
            .map(EndResultedTeam::getTeamId)
            .flatMap(teamRepository::findById)
            .map(entity -> addEndResult(entity, endResultedTeam))
            .flatMap(teamRepository::save)
            .map(teamModelEntityMapper::toModel);
    }

    private TeamEntity addEndResult(final TeamEntity entity, final EndResultedTeam endResultedTeam) {
        entity.setTotalScore(endResultedTeam.getTotalScore());
        entity.setRank(endResultedTeam.getRank());
        entity.setJuniorRank(endResultedTeam.getJuniorRank());
        return entity;
    }
}
