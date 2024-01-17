package hu.bsstudio.robonaut.scores.endresult

import hu.bsstudio.robonaut.scores.endresult.model.EndResultedTeam
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingEndResultServiceTest {
    @MockK
    private lateinit var mockTemplate: RabbitTemplate

    @MockK
    private lateinit var mockService: EndResultService

    private lateinit var underTest: BroadcastingEndResultService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingEndResultService(mockTemplate, mockService)
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it for senior`() {
        every { mockService.updateEndResultSenior(END_RESULTED_TEAM) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateEndResultSenior(END_RESULTED_TEAM)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it for junior`() {
        every { mockService.updateEndResultJunior(END_RESULTED_TEAM) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateEndResultJunior(END_RESULTED_TEAM)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    companion object {
        private val END_RESULTED_TEAM = EndResultedTeam(0, 0)
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam()
        private const val TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
