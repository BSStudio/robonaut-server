package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

final class DefaultSkillTimerServiceTest {

    private static final int TIMER_AT = 2020;

    private DefaultSkillTimerService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new DefaultSkillTimerService();
    }

    @Test
    void shouldReturnSkillTimerWhenTimerIsStarted() {
        final var skillTimer = new SkillTimer(TIMER_AT, TimerAction.START);

        final var result = underTest.startTimer(skillTimer);

        StepVerifier.create(result)
            .expectNext(skillTimer)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenTimerActionDoesNotMatchWhenTimerIsStarted() {
        final var skillTimer = new SkillTimer(TIMER_AT, TimerAction.STOP);

        final var result = underTest.startTimer(skillTimer);

        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void shouldReturnSkillTimerWhenTimerIsStopped() {
        final var skillTimer = new SkillTimer(TIMER_AT, TimerAction.STOP);

        final var result = underTest.stopTimerAt(skillTimer);

        StepVerifier.create(result)
            .expectNext(skillTimer)
            .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenTimerActionDoesNotMatchWhenTimerIsStopped() {
        final var skillTimer = new SkillTimer(TIMER_AT, TimerAction.START);

        final var result = underTest.stopTimerAt(skillTimer);

        StepVerifier.create(result)
            .verifyComplete();
    }
}
