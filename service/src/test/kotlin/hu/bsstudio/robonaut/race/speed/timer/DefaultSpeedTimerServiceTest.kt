package hu.bsstudio.robonaut.race.speed.timer

import hu.bsstudio.robonaut.common.model.TimerAction
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class DefaultSpeedTimerServiceTest {

    private lateinit var underTest: DefaultSpeedTimerService

    @BeforeEach
    internal fun setUp() {
        underTest = DefaultSpeedTimerService()
    }

    @Test
    internal fun shouldReturnTimerOnUpdate() {
        Mono.just(SPEED_TIMER)
            .flatMap(underTest::updateTimer)
            .let(StepVerifier::create)
            .expectNext(SPEED_TIMER)
            .verifyComplete()
    }

    companion object {
        private val SPEED_TIMER = SpeedTimer(2020, TimerAction.START)
    }
}
