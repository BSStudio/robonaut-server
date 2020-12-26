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

final class StopSpeedTimerHandlerTest {

    @Mock
    private SpeedTimerService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        openMocks(this);
        final var underTest = new StopSpeedTimerHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnSpeedTimerWithOkStatus() {
        final var speedTimer = new SpeedTimer(0, TimerAction.STOP);
        when(mockService.stopTimerAt(speedTimer))
            .thenReturn(Mono.just(speedTimer));

        webTestClient.post().uri("/test").bodyValue(speedTimer).exchange()
            .expectStatus().isOk()
            .expectBody(SpeedTimer.class).isEqualTo(speedTimer);
    }
}
