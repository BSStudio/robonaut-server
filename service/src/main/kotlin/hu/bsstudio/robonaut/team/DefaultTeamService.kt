package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Team
import lombok.AccessLevel
import lombok.RequiredArgsConstructor
import lombok.Setter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RequiredArgsConstructor
class DefaultTeamService(private val teamRepository: TeamRepository) : TeamService {

    @Setter(AccessLevel.PACKAGE)
    private val teamMapper = TeamModelEntityMapper()
    override fun addTeam(team: Team): Mono<DetailedTeam> {
        return Mono.just(team)
            .map(this::toEntity)
            .flatMap(teamRepository::insert)
            .map(teamMapper::toModel)
    }

    override fun updateTeam(team: Team): Mono<DetailedTeam> {
        return Mono.just(team)
            .map(Team::teamId)
            .flatMap(teamRepository::findById)
            .map { updateBasicTeamInfo(it, team) }
            .flatMap(teamRepository::save)
            .map(teamMapper::toModel)
    }

    override fun updateTeam(detailedTeam: DetailedTeam): Mono<DetailedTeam> {
        return Mono.just(detailedTeam)
            .map(teamMapper::toEntity)
            .flatMap(teamRepository::save)
            .map(teamMapper::toModel)
    }

    override fun findAllTeam(): Flux<DetailedTeam> {
        return teamRepository.findAll()
            .map(teamMapper::toModel)
    }

    private fun toEntity(team: Team): TeamEntity {
        val defaultScore = ScoreEntity()
        val entity = TeamEntity()
        entity.teamId = team.teamId
        entity.year = team.year
        entity.teamName = team.teamName
        entity.teamMembers = team.teamMembers
        entity.teamType = team.teamType
        entity.speedTimes = emptyList()
        entity.score = defaultScore
        entity.juniorScore = defaultScore
        return entity
    }

    private fun updateBasicTeamInfo(entity: TeamEntity, team: Team): TeamEntity {
        entity.teamId = team.teamId
        entity.year = team.year
        entity.teamName = team.teamName
        entity.teamMembers = team.teamMembers
        entity.teamType = team.teamType
        entity.speedTimes = emptyList()
        return entity
    }
}