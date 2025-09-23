package hu.bsstudio.robonaut.race.skill

import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

interface SkillRaceService {
  fun updateSkillRaceResultOnGate(gateInformation: GateInformation): Mono<DetailedTeam>

  fun updateSkillRaceResult(skillRaceResult: SkillRaceResult): Mono<DetailedTeam>
}
