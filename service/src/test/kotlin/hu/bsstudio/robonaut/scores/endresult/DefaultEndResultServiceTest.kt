package hu.bsstudio.robonaut.scores.endresult

import hu.bsstudio.robonaut.entity.ScoreEntity
import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.entity.TeamType
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultEndResultServiceTest {

    @MockK
    private lateinit var mockRepository: TeamRepository

    @MockK
    private lateinit var mockMapper: TeamModelEntityMapper

    private lateinit var underTest: DefaultEndResultService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultEndResultService(mockRepository, mockMapper)
    }

    @Test
    internal fun shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedOnJunior() {
        val foundTeamEntity = TeamEntity(score = ScoreEntity())
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity(score = ScoreEntity(score = POINTS))
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        every { mockMapper.toModel(updatedTeamEntity) } returns DETAILED_TEAM

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultSenior)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun shouldReturnEmptyWhenEntityWasNotFoundOnJunior() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultSenior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    internal fun shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdatedOnSenior() {
        val foundTeamEntity = TeamEntity(
            teamType = TeamType.JUNIOR,
            juniorScore = ScoreEntity(),
        )
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity(
            juniorScore = ScoreEntity(score = POINTS),
            teamType = TeamType.JUNIOR,
        )
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        every { mockMapper.toModel(updatedTeamEntity) } returns DETAILED_TEAM

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultJunior)
            .let(StepVerifier::create)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun shouldReturnEmptyWhenSeniorTeamTriesToUpdateJuniorScore() {
        val foundTeamEntity = TeamEntity(
            teamType = TeamType.SENIOR,
            juniorScore = ScoreEntity(),
        )
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultJunior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    @Test
    internal fun shouldReturnEmptyWhenEntityWasNotFoundOnSenior() {
        every { mockRepository.findById(TEAM_ID) } returns Mono.empty()

        Mono.just(END_RESULTED_TEAM)
            .flatMap(underTest::updateEndResultJunior)
            .let(StepVerifier::create)
            .verifyComplete()
    }

    companion object {
        private const val TEAM_ID: Long = 42
        private const val POINTS = 420
        private val END_RESULTED_TEAM = EndResultedTeam(TEAM_ID, POINTS)
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam()
    }
}
