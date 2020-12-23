package hu.bsstudio.scores;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.scores.audience.AudienceScoreService;
import hu.bsstudio.scores.audience.model.AudienceScoredTeam;
import hu.bsstudio.team.model.DetailedTeam;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class AudienceScoreHandlerTest {

    @Mock
    private AudienceScoreService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new AudienceScoreHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnDetailedTeamWithOkStatus() {
        final var audienceScoredTeam = new AudienceScoredTeam(0, 0, 0);
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockService.updateAudienceScore(audienceScoredTeam))
            .thenReturn(Mono.just(detailedTeam));

        webTestClient.post().uri("/test").bodyValue(List.of(audienceScoredTeam)).exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<DetailedTeam>>() {}).isEqualTo(List.of(detailedTeam));
    }
}
