package hu.bsstudio.race.speed.timer;

import hu.bsstudio.common.model.TimerAction;
import hu.bsstudio.race.speed.timer.model.SpeedTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

final class DefaultSpeedTimerServiceTest {

    private DefaultSpeedTimerService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new DefaultSpeedTimerService();
    }

    @Test
    void shouldReturnTimerWithZeroInitialValueWhenTimerIsStarted() {
        final var result = underTest.startTimer();

        StepVerifier.create(result)
            .expectNext(new SpeedTimer(0, TimerAction.START))
            .verifyComplete();
    }

    @Test
    void shouldReturnTimerOnStop() {
        final var speedTimer = new SpeedTimer(2020, TimerAction.STOP);

        final var result = underTest.stopTimerAt(speedTimer);

        StepVerifier.create(result)
            .expectNext(speedTimer)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenActionDoesNotMatch() {
        final var speedTimer = new SpeedTimer(2020, TimerAction.START);

        final var result = underTest.stopTimerAt(speedTimer);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
