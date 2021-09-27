package hu.bsstudio.robonaut.scores.endresult

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.entity.TeamType
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

class DefaultEndResultService(
    private val teamRepository: TeamRepository
) : EndResultService {

    internal var teamModelEntityMapper = TeamModelEntityMapper()

    override fun updateEndResultSenior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam> {
        return Mono.just(endResultedTeam)
            .map(EndResultedTeam::teamId)
            .flatMap(teamRepository::findById)
            .map { addEndResultSenior(it, endResultedTeam) }
            .flatMap(teamRepository::save)
            .map(teamModelEntityMapper::toModel)
    }

    override fun updateEndResultJunior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam> {
        return Mono.just(endResultedTeam)
            .map(EndResultedTeam::teamId)
            .flatMap(teamRepository::findById)
            .filter { it.teamType == TeamType.JUNIOR }
            .map { addEndResultJunior(it, endResultedTeam) }
            .flatMap(teamRepository::save)
            .map(teamModelEntityMapper::toModel)
    }

    private fun addEndResultSenior(entity: TeamEntity, endResultedTeam: EndResultedTeam): TeamEntity {
        val score = updateTotalScore(endResultedTeam, entity.score)
        entity.score = score
        return entity
    }

    private fun addEndResultJunior(entity: TeamEntity, endResultedTeam: EndResultedTeam): TeamEntity {
        val juniorScore = updateTotalScore(endResultedTeam, entity.juniorScore)
        entity.juniorScore = juniorScore
        return entity
    }

    private fun updateTotalScore(endResultedTeam: EndResultedTeam, score: ScoreEntity): ScoreEntity {
        score.score = endResultedTeam.totalScore
        return score
    }
}
