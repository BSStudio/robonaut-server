package hu.bsstudio.robonaut.entity

import hu.bsstudio.robonaut.common.model.TeamType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class TeamEntity(
  @Id
  var teamId: Long? = 0,
  var year: Int = 0,
  @Indexed
  var teamName: String = "",
  var teamMembers: List<String> = listOf(),
  var teamType: TeamType = TeamType.JUNIOR,
  // skill
  var skillScore: Int = 0,
  // safetyCar
  var numberOfOvertakes: Int = 0,
  var safetyCarWasFollowed: Boolean = false,
  // speed
  var speedTimes: List<Int> = listOf(),
  // audience
  var votes: Int = 0,
  var audienceScore: Int = 0,
  // qualification
  var qualificationScore: Int = 0,
  var juniorScore: ScoreEntity = ScoreEntity(),
  var score: ScoreEntity = ScoreEntity(),
)
