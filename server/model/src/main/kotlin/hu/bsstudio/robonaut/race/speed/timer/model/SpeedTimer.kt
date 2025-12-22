package hu.bsstudio.robonaut.race.speed.timer.model

import hu.bsstudio.robonaut.common.model.TimerAction

data class SpeedTimer(
  val timerAt: Int,
  val timerAction: TimerAction,
)
