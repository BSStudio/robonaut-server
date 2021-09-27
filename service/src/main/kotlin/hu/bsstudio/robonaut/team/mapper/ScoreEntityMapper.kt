package hu.bsstudio.robonaut.team.mapper

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.team.model.Score

class ScoreEntityMapper {

    fun toEntity(score: Score) = ScoreEntity(
        score = score.totalScore,
        speedScore = score.speedScore,
        bestSpeedTime = score.bestSpeedTime,
    )

    fun toModel(entity: ScoreEntity) = Score(
        speedScore = entity.speedScore,
        bestSpeedTime = entity.bestSpeedTime,
        totalScore = entity.score
    )
}
