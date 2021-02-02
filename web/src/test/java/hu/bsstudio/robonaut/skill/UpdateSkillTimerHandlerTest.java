package hu.bsstudio.robonaut.skill;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService;
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class UpdateSkillTimerHandlerTest {

    private static final SkillTimer SKILL_TIMER = new SkillTimer(0, TimerAction.START);

    @Mock
    private SkillTimerService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        openMocks(this);
        final var underTest = new UpdateSkillTimerHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnSkillTimerWithOkStatus() {
        when(mockService.updateTimer(SKILL_TIMER))
            .thenReturn(Mono.just(SKILL_TIMER));

        webTestClient.post().uri("/test").bodyValue(SKILL_TIMER).exchange()
            .expectStatus().isOk()
            .expectBody(SkillTimer.class).isEqualTo(SKILL_TIMER);
    }
}
