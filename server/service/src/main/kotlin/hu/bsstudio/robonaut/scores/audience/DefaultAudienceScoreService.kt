package hu.bsstudio.robonaut.scores.audience

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import reactor.core.publisher.Mono

class DefaultAudienceScoreService(
    private val teamRepository: TeamRepository,
    private val teamModelEntityMapper: TeamModelEntityMapper = TeamModelEntityMapper(),
) : AudienceScoreService {
    override fun updateAudienceScore(audienceScoredTeam: AudienceScoredTeam): Mono<DetailedTeam> {
        return Mono.just(audienceScoredTeam)
            .map(AudienceScoredTeam::teamId)
            .flatMap(teamRepository::findById)
            .map { addAudienceScore(it, audienceScoredTeam) }
            .flatMap(teamRepository::save)
            .map(teamModelEntityMapper::toModel)
    }

    private fun addAudienceScore(
        entity: TeamEntity,
        audienceScoredTeam: AudienceScoredTeam,
    ): TeamEntity {
        entity.votes = audienceScoredTeam.votes
        entity.audienceScore = audienceScoredTeam.audienceScore
        return entity
    }
}
