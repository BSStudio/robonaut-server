package hu.bsstudio.robonaut.team.mapper

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.team.model.DetailedTeam

class TeamModelEntityMapper(private val scoreEntityMapper: ScoreEntityMapper = ScoreEntityMapper()) {

    fun toModel(teamEntity: TeamEntity) = DetailedTeam(
        teamId = teamEntity.teamId,
        year = teamEntity.year,
        teamName = teamEntity.teamName,
        teamMembers = teamEntity.teamMembers,
        teamType = teamEntity.teamType,
        skillScore = teamEntity.skillScore,
        numberOfOvertakes = teamEntity.numberOfOvertakes,
        safetyCarWasFollowed = teamEntity.safetyCarWasFollowed,
        speedTimes = teamEntity.speedTimes,
        votes = teamEntity.votes,
        audienceScore = teamEntity.audienceScore,
        qualificationScore = teamEntity.qualificationScore,
        combinedScore = scoreEntityMapper.toModel(teamEntity.score),
        juniorScore = scoreEntityMapper.toModel(teamEntity.juniorScore),
    )

    fun toEntity(detailedTeam: DetailedTeam) = TeamEntity(
        teamId = detailedTeam.teamId,
        year = detailedTeam.year,
        teamName = detailedTeam.teamName,
        teamMembers = detailedTeam.teamMembers,
        teamType = detailedTeam.teamType,
        skillScore = detailedTeam.skillScore,
        numberOfOvertakes = detailedTeam.numberOfOvertakes,
        safetyCarWasFollowed = detailedTeam.safetyCarWasFollowed,
        speedTimes = detailedTeam.speedTimes,
        votes = detailedTeam.votes,
        audienceScore = detailedTeam.audienceScore,
        qualificationScore = detailedTeam.qualificationScore,
        score = scoreEntityMapper.toEntity(detailedTeam.combinedScore),
        juniorScore = scoreEntityMapper.toEntity(detailedTeam.juniorScore),
    )
}
