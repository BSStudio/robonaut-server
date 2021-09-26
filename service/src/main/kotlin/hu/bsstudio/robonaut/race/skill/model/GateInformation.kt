package hu.bsstudio.robonaut.race.skill.model

data class GateInformation(
    val teamId: Long,
    val bonusTime: Int,
    val timeLeft: Int,
    val skillScore: Int,
    val totalSkillScore: Int,
)
