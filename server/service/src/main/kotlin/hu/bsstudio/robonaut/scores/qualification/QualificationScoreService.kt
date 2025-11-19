package hu.bsstudio.robonaut.scores.qualification

import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

interface QualificationScoreService {
  fun updateQualificationScore(qualifiedTeam: QualifiedTeam): Mono<DetailedTeam>
}
