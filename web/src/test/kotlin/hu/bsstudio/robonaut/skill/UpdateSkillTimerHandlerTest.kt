package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.common.model.TimerAction
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.RouterFunctions
import reactor.core.publisher.Mono

internal class UpdateSkillTimerHandlerTest {
    @MockK
    private lateinit var mockService: SkillTimerService
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val underTest = UpdateSkillTimerHandler(mockService)
        val routerFunction =
            RouterFunctions.route()
                .POST("/test", underTest).build()
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    internal fun `should return SkillTimer with OK status`() {
        every { mockService.updateTimer(skillTimer) } returns Mono.just(skillTimer)
        webTestClient.post().uri("/test").bodyValue(skillTimer).exchange()
            .expectStatus().isOk
            .expectBody<SkillTimer>().isEqualTo(skillTimer)
    }

    companion object {
        private var skillTimer = SkillTimer(0, TimerAction.START)
    }
}
