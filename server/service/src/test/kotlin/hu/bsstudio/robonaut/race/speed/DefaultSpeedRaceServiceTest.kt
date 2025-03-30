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
        underTest = DefaultSpeedRaceService(mockRepository)
        underTest = DefaultSpeedRaceService(mockRepository, mockMapper)
    }

    @Test
    internal fun `should find, update and return DetailedTeam team on lap`() {
        val foundEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity = TeamEntity(speedTimes = SPEED_TIMES)
        every { mockRepository.save(updatedEntity) } returns Mono.just(updatedEntity)
        every { mockMapper.toModel(updatedEntity) } returns DETAILED_TEAM

        Mono
            .just(SPEED_RACE_SCORE)
            .flatMap(underTest::updateSpeedRaceOnLap)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should return empty when repository returns empty on lap`() {
        val foundEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity = TeamEntity(speedTimes = SPEED_TIMES)
        every { mockRepository.save(updatedEntity) } returns Mono.empty()

        Mono
            .just(SPEED_RACE_SCORE)
            .flatMap(underTest::updateSpeedRaceOnLap)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    internal fun `should find, update and return DetailedTeam on junior speed race`() {
        val foundEntity = TeamEntity(teamType = TeamType.JUNIOR, juniorScore = ScoreEntity())
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity =
            TeamEntity(
                teamType = TeamType.JUNIOR,
                speedTimes = SPEED_TIMES,
                juniorScore = ScoreEntity(speedScore = SPEED_SCORE, bestSpeedTime = BEST_SPEED_TIME),
            )
        every { mockRepository.save(updatedEntity) } returns Mono.just(updatedEntity)
        every { mockMapper.toModel(updatedEntity) } returns DETAILED_TEAM

        Mono
            .just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should return empty when team was not found on junior`() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()
        Mono
            .just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    internal fun `should return empty when senior team tries to update junior result`() {
        val foundEntity = TeamEntity(teamType = TeamType.SENIOR, juniorScore = ScoreEntity())
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        Mono
            .just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceJunior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    internal fun `should find, update and return DetailedTeam on senior speed race`() {
        val foundEntity = TeamEntity(score = ScoreEntity())
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundEntity)
        val updatedEntity =
            TeamEntity(
                speedTimes = SPEED_TIMES,
                score = ScoreEntity(speedScore = SPEED_SCORE, bestSpeedTime = BEST_SPEED_TIME),
            )
        every { mockRepository.save(updatedEntity) } returns Mono.just(updatedEntity)
        every { mockMapper.toModel(updatedEntity) } returns DETAILED_TEAM

        Mono
            .just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceSenior)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should return empty when team was not found on senior`() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()

        Mono
            .just(SPEED_RACE_RESULT)
            .flatMap(underTest::updateSpeedRaceSenior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

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
