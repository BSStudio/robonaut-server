package hu.bsstudio.race.speed;

import hu.bsstudio.race.speed.model.SpeedRaceResult;
import hu.bsstudio.race.speed.model.SpeedRaceScore;
import hu.bsstudio.team.model.DetailedTeam;
import reactor.core.publisher.Mono;

public interface SpeedRaceService {
    Mono<DetailedTeam> updateSpeedRaceOnLap(final SpeedRaceScore speedRaceScore);
    Mono<DetailedTeam> updateSpeedRace(final SpeedRaceResult speedRaceResult);
}
