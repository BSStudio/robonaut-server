package hu.bsstudio.skill;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.race.skill.SkillRaceService;
import hu.bsstudio.race.skill.model.SkillRaceResult;
import hu.bsstudio.team.model.DetailedTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class SkillRaceResultHandlerTest {

    @Mock
    private SkillRaceService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new SkillRaceResultHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnDetailedTeamWithOkStatus() {
        final var skillRaceResult = new SkillRaceResult(0, 0);
        final var detailedTeam = DetailedTeam.builder().build();
        when(mockService.updateSkillRaceResult(skillRaceResult))
            .thenReturn(Mono.just(detailedTeam));

        webTestClient.post().uri("/test").bodyValue(skillRaceResult).exchange()
            .expectStatus().isOk()
            .expectBody(DetailedTeam.class).isEqualTo(detailedTeam);
    }
}
