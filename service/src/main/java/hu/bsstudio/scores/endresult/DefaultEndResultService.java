package hu.bsstudio.scores.endresult;

import hu.bsstudio.entity.TeamEntity;
import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.scores.endresult.model.EndResultedTeam;
import hu.bsstudio.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultEndResultService implements EndResultService {

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
