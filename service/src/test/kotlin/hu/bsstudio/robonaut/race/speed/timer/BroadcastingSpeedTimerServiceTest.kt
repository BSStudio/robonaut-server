package hu.bsstudio.robonaut.race.speed.timer

import hu.bsstudio.robonaut.common.model.TimerAction
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingSpeedTimerServiceTest {

    @MockK
    private lateinit var mockTemplate: RabbitTemplate

    @MockK
    private lateinit var mockService: SpeedTimerService

    private lateinit var underTest: BroadcastingSpeedTimerService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingSpeedTimerService(mockTemplate, mockService)
    }

    @Test
    internal fun shouldReturnSpeedTimerFromUnderLyingServiceAndSendItWhenTimerIsUpdated() {
        every { mockService.updateTimer(SPEED_TIMER) } returns Mono.just(SPEED_TIMER)
        every { mockTemplate.convertAndSend(ROUTING_KEY, SPEED_TIMER) } returns Unit

        Mono.just(SPEED_TIMER)
            .flatMap(underTest::updateTimer)
            .let(StepVerifier::create)
            .expectNext(SPEED_TIMER)
            .verifyComplete()
    }

    companion object {
        private val SPEED_TIMER = SpeedTimer(0, TimerAction.START)
        private const val ROUTING_KEY = "speed.timer"
    }
}
