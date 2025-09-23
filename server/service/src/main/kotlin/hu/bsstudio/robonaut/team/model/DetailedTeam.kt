package hu.bsstudio.robonaut.team.model

import hu.bsstudio.robonaut.entity.TeamType

data class DetailedTeam(
  val teamId: Long = 0,
  val year: Int = 0,
  val teamName: String = "",
  val teamMembers: List<String> = listOf(),
  val teamType: TeamType = TeamType.JUNIOR,
  val skillScore: Int = 0,
  val numberOfOvertakes: Int = 0,
  val safetyCarWasFollowed: Boolean = false,
  val speedTimes: List<Int> = listOf(),
  val votes: Int = 0,
  val audienceScore: Int = 0,
  val qualificationScore: Int = 0,
  val combinedScore: Score = Score(),
  val juniorScore: Score = Score(),
)
