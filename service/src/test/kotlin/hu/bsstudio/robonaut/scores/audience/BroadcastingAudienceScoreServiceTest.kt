package hu.bsstudio.robonaut.scores.audience

import hu.bsstudio.robonaut.scores.audience.model.AudienceScoredTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingAudienceScoreServiceTest {

    @MockK
    private lateinit var mockTemplate: RabbitTemplate
    @MockK
    private lateinit var mockService: AudienceScoreService

    private lateinit var underTest: BroadcastingAudienceScoreService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingAudienceScoreService(mockTemplate, mockService)
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it`() {
        every { mockService.updateAudienceScore(AUDIENCE_SCORED_TEAM) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateAudienceScore(AUDIENCE_SCORED_TEAM)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    companion object {
        private val AUDIENCE_SCORED_TEAM = AudienceScoredTeam(0, 0, 0)
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam()
        private const val TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
