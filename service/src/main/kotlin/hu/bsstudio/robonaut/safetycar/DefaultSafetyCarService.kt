package hu.bsstudio.robonaut.safetycar;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSafetyCarService implements SafetyCarService {

    @NonNull
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
