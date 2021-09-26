package hu.bsstudio.robonaut.safetycar;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class SafetyCarFollowHandlerTest {

    @Mock
    private SafetyCarService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        openMocks(this);
        final var underTest = new SafetyCarFollowHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnDetailedTeamWithOkStatus() {
        final var safetyCarFollowInformation = new SafetyCarFollowInformation(0, true);
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockService.safetyCarWasFollowed(safetyCarFollowInformation))
            .thenReturn(Mono.just(detailedTeam));

        webTestClient.post().uri("/test").bodyValue(safetyCarFollowInformation).exchange()
            .expectStatus().isOk()
            .expectBody(DetailedTeam.class).isEqualTo(detailedTeam);
    }
}
