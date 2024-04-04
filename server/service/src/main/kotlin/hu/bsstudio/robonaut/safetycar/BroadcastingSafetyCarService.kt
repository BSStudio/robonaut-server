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
            .doOnNext(::sendSafetyCarFollow)
            .flatMap(service::safetyCarWasFollowed)
            .doOnNext(::sendTeamData)
    }

    override fun safetyCarWasOvertaken(safetyCarOvertakeInformation: SafetyCarOvertakeInformation): Mono<DetailedTeam> {
        return Mono.just(safetyCarOvertakeInformation)
            .doOnNext(::sendSafetyCarOvertake)
            .flatMap(service::safetyCarWasOvertaken)
            .doOnNext(::sendTeamData)
    }

    private fun sendSafetyCarFollow(safetyCarFollowInformation: SafetyCarFollowInformation) {
        template.convertAndSend("speed.safetyCar.follow", safetyCarFollowInformation)
    }

    private fun sendSafetyCarOvertake(safetyCarOvertakeInformation: SafetyCarOvertakeInformation) {
        template.convertAndSend("speed.safetyCar.overtake", safetyCarOvertakeInformation)
    }

    private fun sendTeamData(detailedTeam: DetailedTeam) {
        template.convertAndSend("team.teamData", detailedTeam)
    }
}
