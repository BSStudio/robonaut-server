package hu.bsstudio.safetycar;

import hu.bsstudio.entity.TeamEntity;
import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSafetyCarService implements SafetyCarService {

    private final TeamRepository repository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper mapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> safetyCarWasFollowed(final SafetyCarFollowInformation followInformation) {
        return Mono.just(followInformation)
            .map(SafetyCarFollowInformation::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateFollowInformation(entity, followInformation))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    @Override
    public Mono<DetailedTeam> safetyCarWasOvertaken(final SafetyCarOvertakeInformation safetyCarOvertakeInformation) {
        return Mono.just(safetyCarOvertakeInformation)
            .map(SafetyCarOvertakeInformation::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateOvertakeInformation(entity, safetyCarOvertakeInformation))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    private TeamEntity updateFollowInformation(final TeamEntity entity, final SafetyCarFollowInformation followInformation) {
        entity.setSafetyCarWasFollowed(followInformation.isSafetyCarFollowed());
        return entity;

    }

    private TeamEntity updateOvertakeInformation(final TeamEntity entity, final SafetyCarOvertakeInformation overtakeInformation) {
        entity.setNumberOfOvertakes(overtakeInformation.getNumberOfOvertakes());
        return entity;
    }
}
