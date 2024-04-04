package hu.bsstudio.robonaut.race.skill

import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingSkillRaceServiceTest {
    @MockK
    private lateinit var mockTemplate: RabbitTemplate

    @MockK
    private lateinit var mockService: SkillRaceService

    private lateinit var underTest: BroadcastingSkillRaceService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingSkillRaceService(mockTemplate, mockService)
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it when race result was submitted`() {
        every { mockService.updateSkillRaceResult(SKILL_RACE_RESULT) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateSkillRaceResult(SKILL_RACE_RESULT)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should send GateInformation then should return DetailedTeam from underlying service and send it`() {
        every { mockTemplate.convertAndSend(GATE_ROUTING_KEY, GATE_INFORMATION) } returns Unit
        every { mockService.updateSkillRaceResultOnGate(GATE_INFORMATION) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateSkillRaceResultOnGate(GATE_INFORMATION)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    companion object {
        private val SKILL_RACE_RESULT = SkillRaceResult(0, 0)
        private val GATE_INFORMATION = GateInformation(0, 0, 0, 0, 0)
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam()
        private const val GATE_ROUTING_KEY = "skill.gate"
        private const val TEAM_ROUTING_KEY = "team.teamData"
    }
}
