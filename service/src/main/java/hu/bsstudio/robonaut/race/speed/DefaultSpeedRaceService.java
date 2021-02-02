package hu.bsstudio.robonaut.race.speed;

import hu.bsstudio.robonaut.entity.TeamEntity;
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult;
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
    public Mono<DetailedTeam> updateSpeedRaceOnLap(final SpeedRaceScore speedRaceScore) {
        return Mono.just(speedRaceScore)
            .map(SpeedRaceScore::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSpeedScore(entity, speedRaceScore))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    @Override
    public Mono<DetailedTeam> updateSpeedRaceJunior(final SpeedRaceResult speedRaceResult) {
        return Mono.just(speedRaceResult)
            .map(SpeedRaceResult::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSpeedScoreJunior(entity, speedRaceResult))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    @Override
    public Mono<DetailedTeam> updateSpeedRaceSenior(final SpeedRaceResult speedRaceResult) {
        return Mono.just(speedRaceResult)
            .map(SpeedRaceResult::getTeamId)
            .flatMap(repository::findById)
            .map(entity -> updateSpeedScoreSenior(entity, speedRaceResult))
            .flatMap(repository::save)
            .map(mapper::toModel);
    }

    private TeamEntity updateSpeedScore(final TeamEntity entity, final SpeedRaceScore score) {
        entity.setSpeedTimes(score.getSpeedTimes());
        return entity;
    }

    private TeamEntity updateSpeedScoreJunior(final TeamEntity entity, final SpeedRaceResult result) {
        final var newJuniorScore = entity.getJuniorScore();
        newJuniorScore.setSpeedScore(result.getSpeedScore());
        newJuniorScore.setBestSpeedTime(result.getBestSpeedTime());

        entity.setJuniorScore(newJuniorScore);
        entity.setSpeedTimes(result.getSpeedTimes());
        return entity;
    }

    private TeamEntity updateSpeedScoreSenior(final TeamEntity entity, final SpeedRaceResult result) {
        final var newScore = entity.getScore();
        newScore.setSpeedScore(result.getSpeedScore());
        newScore.setBestSpeedTime(result.getBestSpeedTime());

        entity.setJuniorScore(newScore);
        entity.setSpeedTimes(result.getSpeedTimes());
        return entity;
    }
}
