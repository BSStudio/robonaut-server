package hu.bsstudio.robonaut.race.skill

import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingSkillRaceService(
    private val template: RabbitTemplate,
    private val service: SkillRaceService
) : SkillRaceService {

    override fun updateSkillRaceResultOnGate(gateInformation: GateInformation): Mono<DetailedTeam> {
        return Mono.just(gateInformation)
            .doOnNext(this::sendGateInfo)
            .flatMap(service::updateSkillRaceResultOnGate)
            .doOnNext(this::sendTeamInfo)
    }

    override fun updateSkillRaceResult(skillRaceResult: SkillRaceResult): Mono<DetailedTeam> {
        return service.updateSkillRaceResult(skillRaceResult)
            .doOnNext(this::sendTeamInfo)
    }

    private fun sendGateInfo(gateInfo: GateInformation) {
        template.convertAndSend(SKILL_GATE_ROUTING_KEY, gateInfo)
    }

    private fun sendTeamInfo(detailedTeam: DetailedTeam) {
        template.convertAndSend(TEAM_TEAM_DATA_ROUTING_KEY, detailedTeam)
    }

    companion object {
        const val SKILL_GATE_ROUTING_KEY = "skill.gate"
        const val TEAM_TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
