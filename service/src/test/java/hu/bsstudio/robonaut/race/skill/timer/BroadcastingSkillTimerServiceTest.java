package hu.bsstudio.robonaut.race.skill.timer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingSkillTimerServiceTest {

    private static final SkillTimer SKILL_TIMER = new SkillTimer(0, null);
    private static final String ROUTING_KEY = "skill.timer";

    private BroadcastingSkillTimerService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private SkillTimerService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingSkillTimerService(mockTemplate, mockService);
    }

    @Test
    void shouldReturnSpeedTimerFromUnderLyingServiceAndSendItWhenTimerIsUpdated() {
        when(mockService.updateTimer(SKILL_TIMER))
            .thenReturn(Mono.just(SKILL_TIMER));

        Mono.just(SKILL_TIMER)
            .flatMap(underTest::updateTimer)
            .as(StepVerifier::create)
            .expectNext(SKILL_TIMER)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(ROUTING_KEY, SKILL_TIMER);
    }
}
