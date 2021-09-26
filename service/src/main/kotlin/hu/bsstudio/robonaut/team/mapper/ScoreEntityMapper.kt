package hu.bsstudio.robonaut.team.mapper

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.team.model.Score

class ScoreEntityMapper {

    fun toEntity(score: Score): ScoreEntity {
        val entity = ScoreEntity()
        entity.score = score.totalScore
        entity.speedScore = score.speedScore
        entity.bestSpeedTime = score.bestSpeedTime
        return entity
    }

    fun toModel(entity: ScoreEntity): Score {
        return Score(entity.speedScore, entity.bestSpeedTime, entity.score)
    }
}
