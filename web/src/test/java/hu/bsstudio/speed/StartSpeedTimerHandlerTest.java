package hu.bsstudio.speed;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.common.model.TimerAction;
import hu.bsstudio.race.speed.timer.SpeedTimerService;
import hu.bsstudio.race.speed.timer.model.SpeedTimer;
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

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new StartSpeedTimerHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnSpeedTimerWithOkStatus() {
        final var speedTimer = new SpeedTimer(0, TimerAction.START);
        when(mockService.startTimer())
            .thenReturn(Mono.just(speedTimer));

        webTestClient.post().uri("/test").exchange()
            .expectStatus().isOk()
            .expectBody(SpeedTimer.class).isEqualTo(speedTimer);
    }
}
