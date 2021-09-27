package hu.bsstudio.robonaut.race.skill.timer;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class DefaultSkillTimerServiceTest {

    private static final SkillTimer SKILL_TIMER = new SkillTimer(2020, TimerAction.START);

    private DefaultSkillTimerService underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new DefaultSkillTimerService();
    }

    @Test
    void shouldReturnSkillTimerWhenTimerIsUpdated() {
        Mono.just(SKILL_TIMER)
            .flatMap(underTest::updateTimer)
            .as(StepVerifier::create)
            .expectNext(SKILL_TIMER)
            .verifyComplete();
    }
}
