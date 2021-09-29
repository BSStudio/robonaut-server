package hu.bsstudio.robonaut.race.speed

import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingSpeedRaceServiceTest {

    @MockK
    private lateinit var mockTemplate: RabbitTemplate
    @MockK
    private lateinit var mockService: SpeedRaceService

    private lateinit var underTest: BroadcastingSpeedRaceService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingSpeedRaceService(mockTemplate, mockService)
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it when RaceResult was submitted on junior`() {
        every { mockService.updateSpeedRaceJunior(SPEED_RACE_RESULT) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateSpeedRaceJunior(SPEED_RACE_RESULT)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should return DetailedTeam from underlying service and send it when RaceResult was submitted on senior`() {
        every { mockService.updateSpeedRaceSenior(SPEED_RACE_RESULT) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateSpeedRaceSenior(SPEED_RACE_RESULT)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    @Test
    internal fun `should send SpeedRaceScore then should return DetailedTeam from underLying service and send it`() {
        every { mockTemplate.convertAndSend(SPEED_LAP_ROUTING_KEY, SPEED_RACE_SCORE) } returns Unit
        every { mockService.updateSpeedRaceOnLap(SPEED_RACE_SCORE) } returns Mono.just(DETAILED_TEAM)
        every { mockTemplate.convertAndSend(TEAM_DATA_ROUTING_KEY, DETAILED_TEAM) } returns Unit

        val result = underTest.updateSpeedRaceOnLap(SPEED_RACE_SCORE)

        StepVerifier.create(result)
            .expectNext(DETAILED_TEAM)
            .verifyComplete()
    }

    companion object {
        private val SPEED_RACE_RESULT = SpeedRaceResult(0, 0, 0, emptyList())
        private val SPEED_RACE_SCORE = SpeedRaceScore(0, emptyList())
        private val DETAILED_TEAM: DetailedTeam = DetailedTeam()
        private const val TEAM_DATA_ROUTING_KEY = "team.teamData"
        private const val SPEED_LAP_ROUTING_KEY = "speed.lap"
    }
}
