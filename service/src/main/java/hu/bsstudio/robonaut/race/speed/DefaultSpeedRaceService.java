package hu.bsstudio.robonaut.race.speed;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore;
import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultSpeedRaceService implements SpeedRaceService {

    @NonNull
    private final TeamRepository repository;

    @Setter(AccessLevel.PACKAGE)
    private TeamModelEntityMapper mapper = new TeamModelEntityMapper();

    @Override
    public Mono<DetailedTeam> updateSpeedRace(final SpeedRaceScore speedRaceScore) {
        return Mono.just(speedRaceScore)
            .map(SpeedRaceScore::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSpeedScore(entity, speedRaceScore))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    private TeamEntity updateSpeedScore(final TeamEntity entity, final SpeedRaceScore score) {
        entity.setSpeedTimes(score.getSpeedTimes());
        return entity;
    }
}
