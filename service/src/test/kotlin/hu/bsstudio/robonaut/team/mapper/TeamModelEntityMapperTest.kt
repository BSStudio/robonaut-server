package hu.bsstudio.robonaut.team.mapper

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.entity.TeamType
import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Score
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TeamModelEntityMapperTest {

    @MockK
    private lateinit var mockMapper: ScoreEntityMapper

    private lateinit var underTest: TeamModelEntityMapper

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = TeamModelEntityMapper()
        underTest.scoreEntityMapper = mockMapper
    }

    @Test
    fun shouldReturnMappedDetailedTeam() {
        every { mockMapper.toModel(SCORE_ENTITY) } returns COMBINED_SCORE
        every { mockMapper.toModel(JUNIOR_SCORE_ENTITY) } returns JUNIOR_SCORE

        val result = underTest.toModel(TEAM_ENTITY)

        Assertions.assertThat(result).isEqualTo(DETAILED_TEAM)
    }

    @Test
    fun shouldReturnMapperEntity() {
        every { mockMapper.toEntity(COMBINED_SCORE) } returns SCORE_ENTITY
        every { mockMapper.toEntity(JUNIOR_SCORE) } returns JUNIOR_SCORE_ENTITY

        val result = underTest.toEntity(DETAILED_TEAM)

        Assertions.assertThat(result).isEqualTo(TEAM_ENTITY)
    }

    companion object {
        private const val TEAM_ID: Long = 42
        private const val YEAR = 2020
        private const val TEAM_NAME = "BSS"
        private val TEAM_MEMBERS = listOf("Bence", "Boldi")
        private val TEAM_TYPE = TeamType.SENIOR
        private const val SKILL_SCORE = 420
        private const val SPEED_SCORE = 16
        private const val NUMBER_OF_OVERTAKES = 1
        private const val SAFETY_CAR_WAS_FOLLOWED = true
        private val SPEED_TIMES = listOf(1, 2)
        private const val VOTES = 999
        private const val AUDIENCE_SCORE = 4
        private const val QUALIFICATION_SCORE = 444
        private const val TOTAL_SCORE = Int.MAX_VALUE
        private const val BEST_SPEED_TIME = 6767
        private val COMBINED_SCORE = Score(SPEED_SCORE, BEST_SPEED_TIME, TOTAL_SCORE)
        private val SCORE_ENTITY = ScoreEntity(SPEED_SCORE, BEST_SPEED_TIME, TOTAL_SCORE)
        private val JUNIOR_SCORE = Score(SPEED_SCORE, BEST_SPEED_TIME, TOTAL_SCORE)
        private val JUNIOR_SCORE_ENTITY = ScoreEntity(SPEED_SCORE, BEST_SPEED_TIME, TOTAL_SCORE)
        private val DETAILED_TEAM =  DetailedTeam(
            teamId = TEAM_ID,
            year = YEAR,
            teamName = TEAM_NAME,
            teamMembers = TEAM_MEMBERS,
            teamType = TEAM_TYPE,
            skillScore = SKILL_SCORE,
            numberOfOvertakes = NUMBER_OF_OVERTAKES,
            safetyCarWasFollowed = SAFETY_CAR_WAS_FOLLOWED,
            speedTimes = SPEED_TIMES,
            votes = VOTES,
            audienceScore = AUDIENCE_SCORE,
            qualificationScore = QUALIFICATION_SCORE,
            combinedScore = COMBINED_SCORE,
            juniorScore = JUNIOR_SCORE,
        )
        private val TEAM_ENTITY = TeamEntity(
            teamId = TEAM_ID,
            year = YEAR,
            teamName = TEAM_NAME,
            teamMembers = TEAM_MEMBERS,
            teamType = TEAM_TYPE,
            skillScore = SKILL_SCORE,
            numberOfOvertakes = NUMBER_OF_OVERTAKES,
            safetyCarWasFollowed = SAFETY_CAR_WAS_FOLLOWED,
            speedTimes = SPEED_TIMES,
            votes = VOTES,
            audienceScore = AUDIENCE_SCORE,
            qualificationScore = QUALIFICATION_SCORE,
            score = SCORE_ENTITY,
            juniorScore = JUNIOR_SCORE_ENTITY,
        )
    }
}
