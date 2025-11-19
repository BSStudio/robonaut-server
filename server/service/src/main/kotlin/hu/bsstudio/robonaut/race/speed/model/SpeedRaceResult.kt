package hu.bsstudio.robonaut.race.speed.model

data class SpeedRaceResult(
  val teamId: Long,
  val speedScore: Int,
  val bestSpeedTime: Int,
  val speedTimes: List<Int>,
)
