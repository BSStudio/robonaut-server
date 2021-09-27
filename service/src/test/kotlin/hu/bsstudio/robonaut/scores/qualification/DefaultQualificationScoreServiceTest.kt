package hu.bsstudio.robonaut.scores.qualification

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultQualificationScoreServiceTest {

    @MockK
    private lateinit var mockRepository: TeamRepository

    @MockK
    private lateinit var mockMapper: TeamModelEntityMapper

    private lateinit var underTest: DefaultQualificationScoreService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultQualificationScoreService(mockRepository)
        underTest.mapper = mockMapper
    }

    @Test
    fun shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdated() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity()
        updatedTeamEntity.qualificationScore = QUALIFICATION_SCORE
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        val detailedTeam = DetailedTeam()
        every { mockMapper.toModel(updatedTeamEntity) } returns detailedTeam

        val result = underTest.updateQualificationScore(QUALIFIED_TEAM)

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyWhenEntityWasNotFound() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity()
        updatedTeamEntity.qualificationScore = QUALIFICATION_SCORE
        every { mockRepository.save(updatedTeamEntity) } returns Mono.empty()

        val result = underTest.updateQualificationScore(QUALIFIED_TEAM)

        StepVerifier.create(result)
            .verifyComplete()
    }

    companion object {
        private const val TEAM_ID: Long = 42
        private const val QUALIFICATION_SCORE = 30
        private val QUALIFIED_TEAM = QualifiedTeam(TEAM_ID, QUALIFICATION_SCORE)
    }
}
