package hu.bsstudio.robonaut.race.speed

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.entity.TeamType
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

class DefaultSpeedRaceService(private val repository: TeamRepository) : SpeedRaceService {

    internal var mapper = TeamModelEntityMapper()

    override fun updateSpeedRaceOnLap(speedRaceScore: SpeedRaceScore): Mono<DetailedTeam> {
        return Mono.just(speedRaceScore)
            .map(SpeedRaceScore::teamId)
            .flatMap(repository::findById)
            .map { updateSpeedScore(it, speedRaceScore) }
            .flatMap(repository::save)
            .map(mapper::toModel)
    }

    override fun updateSpeedRaceJunior(speedRaceResult: SpeedRaceResult): Mono<DetailedTeam> {
        return Mono.just(speedRaceResult)
            .map(SpeedRaceResult::teamId)
            .flatMap(repository::findById)
            .filter { it.teamType == TeamType.JUNIOR }
            .map { updateSpeedScoreJunior(it, speedRaceResult) }
            .flatMap(repository::save)
            .map(mapper::toModel)
    }

    override fun updateSpeedRaceSenior(speedRaceResult: SpeedRaceResult): Mono<DetailedTeam> {
        return Mono.just(speedRaceResult)
            .map(SpeedRaceResult::teamId)
            .flatMap(repository::findById)
            .map { updateSpeedScoreSenior(it, speedRaceResult) }
            .flatMap(repository::save)
            .map(mapper::toModel)
    }

    private fun updateSpeedScore(entity: TeamEntity, score: SpeedRaceScore): TeamEntity {
        entity.speedTimes = score.speedTimes
        return entity
    }

    private fun updateSpeedScoreJunior(entity: TeamEntity, result: SpeedRaceResult): TeamEntity {
        entity.juniorScore = updateScore(result, entity.juniorScore)
        entity.speedTimes = result.speedTimes
        return entity
    }

    private fun updateSpeedScoreSenior(entity: TeamEntity, result: SpeedRaceResult): TeamEntity {
        entity.score = updateScore(result, entity.score)
        entity.speedTimes = result.speedTimes
        return entity
    }

    private fun updateScore(result: SpeedRaceResult, score: ScoreEntity): ScoreEntity {
        score.speedScore = result.speedScore
        score.bestSpeedTime = result.bestSpeedTime
        return score
    }
}
