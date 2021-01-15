package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

final class DefaultSpeedTimerServiceTest {

    private static final SpeedTimer SPEED_TIMER_START = new SpeedTimer(2020, TimerAction.START);
    private static final SpeedTimer SPEED_TIMER_STOP = new SpeedTimer(2020, TimerAction.STOP);

    private DefaultSpeedTimerService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new DefaultSpeedTimerService();
    }

    @Test
    void shouldReturnTimerOnStart() {

        final var result = underTest.startTimer(SPEED_TIMER_START);

        StepVerifier.create(result)
            .expectNext(SPEED_TIMER_START)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenActionDoesNotMatchOnStart() {

        final var result = underTest.startTimer(SPEED_TIMER_STOP);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldReturnTimerOnStop() {

        final var result = underTest.stopTimerAt(SPEED_TIMER_STOP);

        StepVerifier.create(result)
            .expectNext(SPEED_TIMER_STOP)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenActionDoesNotMatchOnStop() {

        final var result = underTest.stopTimerAt(SPEED_TIMER_START);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
