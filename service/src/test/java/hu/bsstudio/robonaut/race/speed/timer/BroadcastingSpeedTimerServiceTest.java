package hu.bsstudio.robonaut.race.speed.timer;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

final class BroadcastingSpeedTimerServiceTest {

    private static final SpeedTimer SPEED_TIMER = new SpeedTimer(0, null);
    private static final String ROUTING_KEY = "speed.timer";

    private BroadcastingSpeedTimerService underTest;

    @Mock
    private RabbitTemplate mockTemplate;
    @Mock
    private SpeedTimerService mockService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        this.underTest = new BroadcastingSpeedTimerService(mockTemplate, mockService);
    }

    @Test
    void shouldReturnSpeedTimerFromUnderLyingServiceAndSendItWhenTimerIsStarted() {
        when(mockService.startTimer())
            .thenReturn(Mono.just(SPEED_TIMER));

        final var result = underTest.startTimer();

        StepVerifier.create(result)
            .expectNext(SPEED_TIMER)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(ROUTING_KEY, SPEED_TIMER);
    }

    @Test
    void shouldReturnSpeedTimerFromUnderLyingServiceAndSendItWhenTimerIsStopped() {
        when(mockService.stopTimerAt(SPEED_TIMER))
            .thenReturn(Mono.just(SPEED_TIMER));

        final var result = underTest.stopTimerAt(SPEED_TIMER);

        StepVerifier.create(result)
            .expectNext(SPEED_TIMER)
            .verifyComplete();
        verify(mockTemplate).convertAndSend(ROUTING_KEY, SPEED_TIMER);
    }
}
