package hu.bsstudio.robonaut.safetycar;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class SafetyCarOvertakeHandlerTest {

    @Mock
    private SafetyCarService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new SafetyCarOvertakeHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnDetailedTeamWithOkStatus() {
        final var safetyCarOvertakeInformation = new SafetyCarOvertakeInformation(0, 0);
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockService.safetyCarWasOvertaken(safetyCarOvertakeInformation))
            .thenReturn(Mono.just(detailedTeam));

        webTestClient.post().uri("/test").bodyValue(safetyCarOvertakeInformation).exchange()
            .expectStatus().isOk()
            .expectBody(DetailedTeam.class).isEqualTo(detailedTeam);
    }
}
