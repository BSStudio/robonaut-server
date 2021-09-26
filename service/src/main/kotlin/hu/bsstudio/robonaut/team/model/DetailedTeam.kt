package hu.bsstudio.robonaut.team.model

import hu.bsstudio.robonaut.entity.TeamType

data class DetailedTeam (
    val teamId: Long,
    val year: Int,
    val teamName: String,
    val teamMembers: List<String>,
    val teamType: TeamType,
    val skillScore: Int,
    val numberOfOvertakes: Int,
    val safetyCarWasFollowed: Boolean,
    val speedTimes: List<Int>,
    val votes: Int,
    val audienceScore: Int,
    val qualificationScore: Int,
    val combinedScore: Score,
    val juniorScore: Score,
)
