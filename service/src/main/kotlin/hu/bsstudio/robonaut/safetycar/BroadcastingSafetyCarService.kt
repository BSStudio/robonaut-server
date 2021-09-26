package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingSafetyCarService(
    private val template: RabbitTemplate,
    private val service: SafetyCarService,
) : SafetyCarService {

    override fun safetyCarWasFollowed(safetyCarFollowInformation: SafetyCarFollowInformation): Mono<DetailedTeam> {
        return Mono.just(safetyCarFollowInformation)
            .doOnNext(this::sendSafetyCarFollow)
            .flatMap(service::safetyCarWasFollowed)
            .doOnNext(this::sendTeamData)
    }

    override fun safetyCarWasOvertaken(safetyCarOvertakeInformation: SafetyCarOvertakeInformation): Mono<DetailedTeam> {
        return Mono.just(safetyCarOvertakeInformation)
            .doOnNext(this::sendSafetyCarOvertake)
            .flatMap(service::safetyCarWasOvertaken)
            .doOnNext(this::sendTeamData)
    }

    private fun sendSafetyCarFollow(safetyCarFollowInformation: SafetyCarFollowInformation) {
        template.convertAndSend(SPEED_SAFETY_CAR_FOLLOW_ROUTING_KEY, safetyCarFollowInformation)
    }

    private fun sendSafetyCarOvertake(safetyCarOvertakeInformation: SafetyCarOvertakeInformation) {
        template.convertAndSend(SPEED_SAFETY_CAR_OVERTAKE_ROUTING_KEY, safetyCarOvertakeInformation)
    }

    private fun sendTeamData(detailedTeam: DetailedTeam) {
        template.convertAndSend(TEAM_DATA_ROUTING_KEY, detailedTeam)
    }

    companion object {
        const val SPEED_SAFETY_CAR_FOLLOW_ROUTING_KEY = "speed.safetyCar.follow"
        const val SPEED_SAFETY_CAR_OVERTAKE_ROUTING_KEY = "speed.safetyCar.overtake"
        const val TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
