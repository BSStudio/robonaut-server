package hu.bsstudio.robonaut.team.model

import hu.bsstudio.robonaut.common.model.TeamType
import java.util.Calendar

data class Team(
  val teamId: Long = 0,
  val year: Int = Calendar.getInstance().get(Calendar.YEAR),
  val teamName: String = "",
  val teamMembers: List<String> = listOf(),
  val teamType: TeamType = TeamType.JUNIOR,
)
