package hu.bsstudio.robonaut.scores;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService;
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam;
import hu.bsstudio.robonaut.team.model.DetailedTeam;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class QualificationScoreHandlerTest {

    @Mock
    private QualificationScoreService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new QualificationScoreHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnDetailedTeamWithOkStatus() {
        final var qualifiedTeam = new QualifiedTeam(0, 0);
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockService.updateQualificationScore(qualifiedTeam))
            .thenReturn(Mono.just(detailedTeam));

        webTestClient.post().uri("/test").bodyValue(qualifiedTeam).exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<DetailedTeam>>() {
            }).isEqualTo(List.of(detailedTeam));
    }
}
