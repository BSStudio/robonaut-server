package hu.bsstudio.robonaut.race.skill

import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import hu.bsstudio.robonaut.team.model.DetailedTeam
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono

class BroadcastingSkillRaceService(
  private val template: RabbitTemplate,
  private val service: SkillRaceService,
) : SkillRaceService {
  override fun updateSkillRaceResultOnGate(gateInformation: GateInformation): Mono<DetailedTeam> =
    Mono
      .just(gateInformation)
      .doOnNext(::sendGateInfo)
      .flatMap(service::updateSkillRaceResultOnGate)
      .doOnNext(::sendTeamInfo)

  override fun updateSkillRaceResult(skillRaceResult: SkillRaceResult): Mono<DetailedTeam> =
    service
      .updateSkillRaceResult(skillRaceResult)
      .doOnNext(::sendTeamInfo)

  private fun sendGateInfo(gateInfo: GateInformation) {
    template.convertAndSend("skill.gate", gateInfo)
  }

  private fun sendTeamInfo(detailedTeam: DetailedTeam) {
    template.convertAndSend("team.teamData", detailedTeam)
  }
}
