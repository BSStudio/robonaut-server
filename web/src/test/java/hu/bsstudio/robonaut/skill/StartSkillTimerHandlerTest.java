package hu.bsstudio.robonaut.skill;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import hu.bsstudio.robonaut.common.model.TimerAction;
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService;
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

final class StartSkillTimerHandlerTest {

    @Mock
    private SkillTimerService mockService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        initMocks(this);
        final var underTest = new StartSkillTimerHandler(mockService);
        final var routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldReturnSkillTimerWithOkStatus() {
        final var skillTimer = new SkillTimer(0, TimerAction.START);
        when(mockService.startTimer(skillTimer))
            .thenReturn(Mono.just(skillTimer));

        webTestClient.post().uri("/test").bodyValue(skillTimer).exchange()
            .expectStatus().isOk()
            .expectBody(SkillTimer.class).isEqualTo(skillTimer);
    }
}
