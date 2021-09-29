package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Team
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingTeamServiceTest {

    @MockK
    private lateinit var mockTemplate: RabbitTemplate
    @MockK
    private lateinit var mockService: TeamService

    private lateinit var underTest: BroadcastingTeamService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingTeamService(mockTemplate, mockService)
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it when Team is created`() {
        every { mockService.addTeam(TEAM) } returns Mono.just(DETAILED_TEAM_1)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1) } returns Unit

        val result = underTest.addTeam(TEAM)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete()
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it when Team is updated`() {
        every { mockService.updateTeam(TEAM) } returns Mono.just(DETAILED_TEAM_1)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1) } returns Unit

        val result = underTest.updateTeam(TEAM)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete()
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it when Team is updated by admin`() {
        every { mockService.updateTeam(DETAILED_TEAM_1) } returns Mono.just(DETAILED_TEAM_1)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1) } returns Unit

        val result = underTest.updateTeam(DETAILED_TEAM_1)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .verifyComplete()
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it when finding all Team`() {
        every { mockService.findAllTeam() } returns Flux.just(DETAILED_TEAM_1, DETAILED_TEAM_2)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_1) } returns Unit
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM_2) } returns Unit

        val result = underTest.findAllTeam()

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM_1)
            .expectNext(DETAILED_TEAM_2)
            .verifyComplete()
    }

    companion object {
        private val TEAM: Team = Team()
        private val DETAILED_TEAM_1: DetailedTeam = DetailedTeam(teamId = 1)
        private val DETAILED_TEAM_2: DetailedTeam = DetailedTeam(teamId = 2)
        private const val TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
