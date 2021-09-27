package hu.bsstudio.robonaut.team.mapper

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.team.model.Score
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ScoreEntityMapperTest {

    companion object {
        private const val SCORE = 420
        private const val SPEED_SCORE = 2021
        private const val BEST_SPEED_TIME = 999
        private val MODEL = Score(SPEED_SCORE, BEST_SPEED_TIME, SCORE)
        private val ENTITY = ScoreEntity(
            speedScore = SPEED_SCORE,
            bestSpeedTime = BEST_SPEED_TIME,
            score = SCORE,
        )
    }

    private lateinit var underTest: ScoreEntityMapper

    @BeforeEach
    fun setUp() {
        underTest = ScoreEntityMapper()
    }

    @Test
    fun shouldReturnModel() {
        val result = underTest.toModel(ENTITY)

        Assertions.assertThat(result).isEqualTo(MODEL)
    }

    @Test
    fun shouldReturnEntity() {
        val result = underTest.toEntity(MODEL)

        Assertions.assertThat(result).isEqualTo(ENTITY)
    }
}
