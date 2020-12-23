package hu.bsstudio.team;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.team.model.DetailedTeam;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Flux;

final class ReadAllTeamHandlerTest {

    @Mock
    private TeamService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new ReadAllTeamHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnDetailedTeamWithOkStatus() {
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockService.findAllTeam())
            .thenReturn(Flux.just(detailedTeam));

        webTestClient.post().uri("/test").exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<DetailedTeam>>() {}).isEqualTo(List.of(detailedTeam));
    }
}
