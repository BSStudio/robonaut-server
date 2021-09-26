package hu.bsstudio.robonaut.race.speed

import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

interface SpeedRaceService {
    fun updateSpeedRaceOnLap(speedRaceScore: SpeedRaceScore): Mono<DetailedTeam>
    fun updateSpeedRaceJunior(speedRaceResult: SpeedRaceResult): Mono<DetailedTeam>
    fun updateSpeedRaceSenior(speedRaceResult: SpeedRaceResult): Mono<DetailedTeam>
}
