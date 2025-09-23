package hu.bsstudio.robonaut.entity

data class ScoreEntity(
  // speed
  var speedScore: Int = 0,
  var bestSpeedTime: Int = 0,
  // calculated
  var score: Int = 0,
)
