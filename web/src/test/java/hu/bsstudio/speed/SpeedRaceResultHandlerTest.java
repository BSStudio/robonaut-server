package hu.bsstudio.speed;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.race.speed.model.SpeedRaceResult;
import hu.bsstudio.team.model.DetailedTeam;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class SpeedRaceResultHandlerTest {

    @Mock
    private SpeedRaceService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new SpeedRaceResultHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnDetailedTeamWithOkStatus() {
        final var speedRaceResult = new SpeedRaceResult(0, 0, 0, Collections.emptyList());
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockService.updateSpeedRace(speedRaceResult))
            .thenReturn(Mono.just(detailedTeam));

        webTestClient.post().uri("/test").bodyValue(speedRaceResult).exchange()
            .expectStatus().isOk()
            .expectBody(DetailedTeam.class).isEqualTo(detailedTeam);
    }
}
