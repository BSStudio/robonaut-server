package hu.bsstudio.robonaut.team.model

import hu.bsstudio.robonaut.common.model.TeamType

data class Team(
  val teamId: Long = 0,
  val year: Int = 0,
  val teamName: String = "",
  val teamMembers: List<String> = listOf(),
  val teamType: TeamType = TeamType.JUNIOR,
)
