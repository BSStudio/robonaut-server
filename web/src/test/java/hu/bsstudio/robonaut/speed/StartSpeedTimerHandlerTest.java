package hu.bsstudio.robonaut.speed;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService;
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class StartSpeedTimerHandlerTest {

    @Mock
    private SpeedTimerService mockService;

    private WebTestClient webTestClient;
    public static final SpeedTimer SPEED_TIMER = new SpeedTimer(0, TimerAction.START);

    @BeforeEach
    void setUp() {
        openMocks(this);
        final var underTest = new StartSpeedTimerHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnSpeedTimerWithOkStatus() {
        when(mockService.startTimer(SPEED_TIMER))
            .thenReturn(Mono.just(SPEED_TIMER));

        webTestClient.post().uri("/test").exchange()
            .expectStatus().isOk()
            .expectBody(SpeedTimer.class).isEqualTo(SPEED_TIMER);
    }
}
