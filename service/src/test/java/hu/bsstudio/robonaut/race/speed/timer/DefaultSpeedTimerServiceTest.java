package hu.bsstudio.robonaut.race.speed.timer;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultSpeedTimerServiceTest {

    private static final SpeedTimer SPEED_TIMER = new SpeedTimer(2020, TimerAction.START);

    private DefaultSpeedTimerService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new DefaultSpeedTimerService();
    }

    @Test
    void shouldReturnTimerOnUpdate() {
        Mono.just(SPEED_TIMER)
            .flatMap(underTest::updateTimer)
            .as(StepVerifier::create)
            .expectNext(SPEED_TIMER)
            .verifyComplete();
    }
}
