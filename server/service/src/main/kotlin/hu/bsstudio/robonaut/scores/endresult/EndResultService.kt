package hu.bsstudio.robonaut.scores.endresult

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

interface EndResultService {
  fun updateEndResultSenior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam>

  fun updateEndResultJunior(endResultedTeam: EndResultedTeam): Mono<DetailedTeam>
}
