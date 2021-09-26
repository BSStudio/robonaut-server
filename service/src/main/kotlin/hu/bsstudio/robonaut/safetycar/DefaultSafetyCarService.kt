package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

class DefaultSafetyCarService(private val repository: TeamRepository) : SafetyCarService {

    var mapper = TeamModelEntityMapper()

    override fun safetyCarWasFollowed(safetyCarFollowInformation: SafetyCarFollowInformation): Mono<DetailedTeam> {
        return Mono.just(safetyCarFollowInformation)
            .map(SafetyCarFollowInformation::teamId)
            .flatMap(repository::findById)
            .map { updateFollowInformation(it, safetyCarFollowInformation) }
            .flatMap(repository::save)
            .map(mapper::toModel)
    }

    override fun safetyCarWasOvertaken(safetyCarOvertakeInformation: SafetyCarOvertakeInformation): Mono<DetailedTeam> {
        return Mono.just(safetyCarOvertakeInformation)
            .map(SafetyCarOvertakeInformation::teamId)
            .flatMap(repository::findById)
            .map { updateOvertakeInformation(it, safetyCarOvertakeInformation) }
            .flatMap(repository::save)
            .map(mapper::toModel)
    }

    private fun updateFollowInformation(entity: TeamEntity, followInformation: SafetyCarFollowInformation): TeamEntity {
        entity.isSafetyCarWasFollowed = followInformation.safetyCarFollowed
        return entity
    }

    private fun updateOvertakeInformation(entity: TeamEntity, overtakeInformation: SafetyCarOvertakeInformation): TeamEntity {
        entity.numberOfOvertakes = overtakeInformation.numberOfOvertakes
        return entity
    }
}
