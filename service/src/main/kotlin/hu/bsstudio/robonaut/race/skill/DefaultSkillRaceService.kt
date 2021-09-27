package hu.bsstudio.robonaut.race.skill

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

class DefaultSkillRaceService(private val repository: TeamRepository) : SkillRaceService {

    internal var mapper = TeamModelEntityMapper()

    override fun updateSkillRaceResultOnGate(gateInformation: GateInformation): Mono<DetailedTeam> {
        return Mono.just(gateInformation)
            .map(GateInformation::teamId)
            .flatMap(repository::findById)
            .map { updateSkillRaceInfo(it, gateInformation) }
            .flatMap(repository::save)
            .map(mapper::toModel)
    }

    override fun updateSkillRaceResult(skillRaceResult: SkillRaceResult): Mono<DetailedTeam> {
        return Mono.just(skillRaceResult)
            .map(SkillRaceResult::teamId)
            .flatMap(repository::findById)
            .map { updateSkillRaceInfo(it, skillRaceResult) }
            .flatMap(repository::save)
            .map(mapper::toModel)
    }

    private fun updateSkillRaceInfo(entity: TeamEntity, gateInformation: GateInformation): TeamEntity {
        entity.skillScore = gateInformation.totalSkillScore
        return entity
    }

    private fun updateSkillRaceInfo(entity: TeamEntity, skillRaceResult: SkillRaceResult): TeamEntity {
        entity.skillScore = skillRaceResult.skillScore
        return entity
    }
}
