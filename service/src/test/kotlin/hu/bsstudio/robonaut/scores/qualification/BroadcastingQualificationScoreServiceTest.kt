package hu.bsstudio.robonaut.scores.qualification

import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingQualificationScoreServiceTest {
    @MockK
    private lateinit var mockTemplate: RabbitTemplate

    @MockK
    private lateinit var mockService: QualificationScoreService

    private lateinit var underTest: BroadcastingQualificationScoreService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingQualificationScoreService(mockTemplate, mockService)
    }

    @Test
    internal fun `should return DetailedTeam from under lying service and send it`() {
        every { mockService.updateQualificationScore(QUALIFIED_TEAM) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateQualificationScore(QUALIFIED_TEAM)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    companion object {
        private val QUALIFIED_TEAM = QualifiedTeam(0, 0)
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam()
        private const val TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
