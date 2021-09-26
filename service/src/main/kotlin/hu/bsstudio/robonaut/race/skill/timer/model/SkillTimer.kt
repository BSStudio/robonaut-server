package hu.bsstudio.robonaut.race.skill.timer.model

import hu.bsstudio.robonaut.common.model.TimerAction

data class SkillTimer(
    val timerAt: Int,
    val timerAction: TimerAction
)
