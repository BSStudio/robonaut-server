package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

interface SafetyCarService {
    fun safetyCarWasFollowed(safetyCarFollowInformation: SafetyCarFollowInformation): Mono<DetailedTeam>
    fun safetyCarWasOvertaken(safetyCarOvertakeInformation: SafetyCarOvertakeInformation): Mono<DetailedTeam>
}
