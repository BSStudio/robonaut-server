package hu.bsstudio.robonaut.scores.qualification

import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import lombok.RequiredArgsConstructor
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.entity.TeamEntity
import reactor.core.publisher.Mono

@RequiredArgsConstructor
class DefaultQualificationScoreService(private val teamRepository: TeamRepository) : QualificationScoreService {

    internal var mapper = TeamModelEntityMapper()

    override fun updateQualificationScore(qualifiedTeam: QualifiedTeam): Mono<DetailedTeam> {
        return Mono.just(qualifiedTeam)
            .map(QualifiedTeam::teamId)
            .flatMap(teamRepository::findById)
            .map { addQualificationScore(it, qualifiedTeam) }
            .flatMap(teamRepository::save)
            .map(mapper::toModel)
    }

    private fun addQualificationScore(entity: TeamEntity, qualifiedTeam: QualifiedTeam): TeamEntity {
        entity.qualificationScore = qualifiedTeam.qualificationScore
        return entity
    }
}
