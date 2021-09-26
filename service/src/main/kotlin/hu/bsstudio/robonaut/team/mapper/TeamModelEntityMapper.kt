package hu.bsstudio.robonaut.team.mapper

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.team.model.DetailedTeam
import lombok.AccessLevel
import lombok.Setter

class TeamModelEntityMapper {
    @Setter(AccessLevel.PACKAGE)
    private val scoreEntityMapper = ScoreEntityMapper()
    fun toModel(teamEntity: TeamEntity): DetailedTeam {
        return DetailedTeam(
            teamId = teamEntity.teamId,
            year = teamEntity.year,
            teamName = teamEntity.teamName,
            teamMembers = teamEntity.teamMembers,
            teamType = teamEntity.teamType,
            skillScore = teamEntity.skillScore,
            numberOfOvertakes = teamEntity.numberOfOvertakes,
            safetyCarWasFollowed = teamEntity.isSafetyCarWasFollowed,
            speedTimes = teamEntity.speedTimes,
            votes = teamEntity.votes,
            audienceScore = teamEntity.audienceScore,
            qualificationScore = teamEntity.qualificationScore,
            combinedScore = scoreEntityMapper.toModel(teamEntity.score),
            juniorScore = scoreEntityMapper.toModel(teamEntity.juniorScore),
        )
    }

    fun toEntity(detailedTeam: DetailedTeam): TeamEntity {
        val entity = TeamEntity()
        entity.teamId = detailedTeam.teamId
        entity.year = detailedTeam.year
        entity.teamName = detailedTeam.teamName
        entity.teamMembers = detailedTeam.teamMembers
        entity.teamType = detailedTeam.teamType
        entity.skillScore = detailedTeam.skillScore
        entity.numberOfOvertakes = detailedTeam.numberOfOvertakes
        entity.isSafetyCarWasFollowed = detailedTeam.safetyCarWasFollowed
        entity.speedTimes = detailedTeam.speedTimes
        entity.votes = detailedTeam.votes
        entity.audienceScore = detailedTeam.audienceScore
        entity.qualificationScore = detailedTeam.qualificationScore
        entity.score = scoreEntityMapper.toEntity(detailedTeam.combinedScore)
        entity.juniorScore = scoreEntityMapper.toEntity(detailedTeam.juniorScore)
        return entity
    }
}
