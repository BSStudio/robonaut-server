package hu.bsstudio.robonaut.scores.audience

import hu.bsstudio.robonaut.entity.TeamEntity
import hu.bsstudio.robonaut.repository.TeamRepository
import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import hu.bsstudio.robonaut.team.mapper.TeamModelEntityMapper
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultAudienceScoreServiceTest {

    @MockK
    private lateinit var mockRepository: TeamRepository

    @MockK
    private lateinit var mockMapper: TeamModelEntityMapper

    private lateinit var underTest: DefaultAudienceScoreService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = DefaultAudienceScoreService(mockRepository, mockMapper)
    }

    @Test
    internal fun shouldReturnDetailedTeamWhenEntityWasFoundAndSuccessfullyWasUpdated() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity(audienceScore = AUDIENCE_SCORE, votes = VOTES)
        every { mockRepository.save(updatedTeamEntity) } returns Mono.just(updatedTeamEntity)
        val detailedTeam = DetailedTeam()
        every { mockMapper.toModel(updatedTeamEntity) } returns detailedTeam

        val result = underTest.updateAudienceScore(AUDIENCE_SCORED_TEAM)

        StepVerifier.create(result)
            .expectNext(detailedTeam)
            .verifyComplete()
    }

    @Test
    internal fun shouldReturnEmptyWhenEntityWasNotFound() {
        val foundTeamEntity = TeamEntity()
        every { mockRepository.findById(TEAM_ID) } returns Mono.just(foundTeamEntity)
        val updatedTeamEntity = TeamEntity(audienceScore = AUDIENCE_SCORE, votes = VOTES)
        every { mockRepository.save(updatedTeamEntity) } returns Mono.empty()

        val result = underTest.updateAudienceScore(AUDIENCE_SCORED_TEAM)

        StepVerifier.create(result)
            .verifyComplete()
    }

    companion object {
        private const val TEAM_ID: Long = 42
        private const val VOTES = 420
        private const val AUDIENCE_SCORE = 20
        private val AUDIENCE_SCORED_TEAM = AudienceScoredTeam(TEAM_ID, VOTES, AUDIENCE_SCORE)
    }
}
