package hu.bsstudio.robonaut.race.speed;

import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface SpeedRaceService {
    Mono<DetailedTeam> updateSpeedRace(final SpeedRaceScore speedRaceScore);
}
