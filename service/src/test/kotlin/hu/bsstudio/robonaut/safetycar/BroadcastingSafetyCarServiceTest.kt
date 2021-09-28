package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingSafetyCarServiceTest {

    @MockK
    private lateinit var mockTemplate: RabbitTemplate
    @MockK
    private lateinit var mockService: SafetyCarService

    private lateinit var underTest: BroadcastingSafetyCarService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingSafetyCarService(mockTemplate, mockService)
    }

    @Test
    internal fun `should send SafetyCarFollowInformation then should return DetailedTeam from underlying service and send it`() {
        every { mockTemplate.convertAndSend(FOLLOW_ROUTING_KEY, SAFETY_CAR_FOLLOW_INFORMATION) } returns Unit
        every { mockService.safetyCarWasFollowed(SAFETY_CAR_FOLLOW_INFORMATION) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.safetyCarWasFollowed(SAFETY_CAR_FOLLOW_INFORMATION)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should send SafetyCarOvertakeInformation then should return DetailedTeam from underlying service and send it`() {
        every { mockTemplate.convertAndSend(OVERTAKE_ROUTING_KEY, SAFETY_CAR_OVERTAKE_INFORMATION) } returns Unit
        every { mockService.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.safetyCarWasOvertaken(SAFETY_CAR_OVERTAKE_INFORMATION)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    companion object {
        private val SAFETY_CAR_FOLLOW_INFORMATION = SafetyCarFollowInformation(0, true)
        private val SAFETY_CAR_OVERTAKE_INFORMATION = SafetyCarOvertakeInformation(0, 0)
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam(teamId = 0)
        private const val FOLLOW_ROUTING_KEY = "speed.safetyCar.follow"
        private const val OVERTAKE_ROUTING_KEY = "speed.safetyCar.overtake"
        private const val TEAM_DATA_ROUTING_KEY = "team.teamData"
    }
}
