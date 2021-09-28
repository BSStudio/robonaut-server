package hu.bsstudio.robonaut.race.speed

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.entity.TeamType
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultSpeedRaceServiceTest {

    @MockK
    private lateinit var mockRepository: TeamRepository
    @MockK
    private lateinit var mockMapper: TeamModelEntityMapper

    private lateinit var underTest: DefaultSpeedRaceService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultSpeedRaceService(mockRepository, mockMapper)
    }

    @Test
    fun onLap() {
        val foundEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity = TeamEntity()
        updatedEntity.speedTimes = SPEED_TIMES
        every { mockRepository.save(updatedEntity) } returns Mono.just(updatedEntity)
        every { mockMapper.toModel(updatedEntity) } returns DETAILED_TEAM

        Mono.just(SPEED_RACE_SCORE)
            .flatMap(underTest::updateSpeedRaceOnLap)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    fun onLapNotFound() {
        val foundEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity = TeamEntity()
        updatedEntity.speedTimes = SPEED_TIMES
        every { mockRepository.save(updatedEntity) } returns Mono.empty()

        Mono.just(SPEED_RACE_SCORE)
            .flatMap(underTest::updateSpeedRaceOnLap)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    fun junior() {
        val juniorScore = ScoreEntity()
        val foundEntity = TeamEntity(teamType = TeamType.JUNIOR, juniorScore = juniorScore)
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity = TeamEntity(teamType = TeamType.JUNIOR, speedTimes = SPEED_TIMES)
        juniorScore.speedScore = SPEED_SCORE
        juniorScore.bestSpeedTime = BEST_SPEED_TIME
        updatedEntity.juniorScore = juniorScore
        every { mockRepository.save(updatedEntity) } returns Mono.just(updatedEntity)
        every { mockMapper.toModel(updatedEntity) } returns DETAILED_TEAM

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    fun juniorNotFound() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()
        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    fun juniorWhenSenior() {
        val foundEntity = TeamEntity()
        foundEntity.teamType = TeamType.SENIOR
        val juniorScore = ScoreEntity()
        foundEntity.juniorScore = juniorScore
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    fun senior() {
        val scoreEntity = ScoreEntity()
        val foundEntity = TeamEntity(score = scoreEntity)
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity = TeamEntity()
        updatedEntity.speedTimes = SPEED_TIMES
        scoreEntity.speedScore = SPEED_SCORE
        scoreEntity.bestSpeedTime = BEST_SPEED_TIME
        updatedEntity.score = scoreEntity
        every { mockRepository.save(updatedEntity) } returns Mono.just(updatedEntity)
        every { mockMapper.toModel(updatedEntity) } returns DETAILED_TEAM

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceSenior)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    fun seniorNotFound() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()

        Mono.just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceSenior)
            .let(StepVerifier::create)
            .verifyComplete()
    } // todo rename

    companion object {
        private const val TEAM_ID: Long = 1
        private const val SPEED_SCORE = 420
        private const val BEST_SPEED_TIME = 444
        private val SPEED_TIMES = listOf(123, 456)
        private val SPEED_RACE_RESULT = SpeedRaceResult(TEAM_ID, SPEED_SCORE, BEST_SPEED_TIME, SPEED_TIMES)
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam(teamId = TEAM_ID)
        private val SPEED_RACE_SCORE = SpeedRaceScore(TEAM_ID, SPEED_TIMES)
    }
}
