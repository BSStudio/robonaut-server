package hu.bsstudio.robonaut.race.skill.timer

import hu.bsstudio.robonaut.common.model.TimerAction
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class BroadcastingSkillTimerServiceTest {

    @MockK
    private lateinit var mockTemplate: RabbitTemplate
    @MockK
    private lateinit var mockService: SkillTimerService

    private lateinit var underTest: BroadcastingSkillTimerService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = BroadcastingSkillTimerService(mockTemplate, mockService)
    }

    @Test
    internal fun `should return SpeedTimer from underlying service and send it when timer is updated`() {
        every { mockTemplate.convertAndSend(ROUTING_KEY, SKILL_TIMER) } returns Unit
        every { mockService.updateTimer(SKILL_TIMER) } returns Mono.just(SKILL_TIMER)

        Mono.just(SKILL_TIMER)
            .flatMap(underTest::updateTimer)
            .let(StepVerifier::create)
            .expectNext(SKILL_TIMER)
            .verifyComplete()
    }

    companion object {
        private val SKILL_TIMER = SkillTimer(0, TimerAction.START)
        private const val ROUTING_KEY = "skill.timer"
    }
}
