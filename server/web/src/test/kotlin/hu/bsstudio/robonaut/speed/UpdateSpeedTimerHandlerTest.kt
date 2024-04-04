package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.common.model.TimerAction
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.RouterFunctions
import reactor.core.publisher.Mono

internal class UpdateSpeedTimerHandlerTest {
    @MockK
    private lateinit var mockService: SpeedTimerService
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val underTest = UpdateSpeedTimerHandler(mockService)
        val routerFunction =
            RouterFunctions.route()
                .POST("/test", underTest).build()
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    internal fun `should return SpeedTimer with OK status`() {
        every { mockService.updateTimer(speedTimer) } returns Mono.just(speedTimer)

        webTestClient.post().uri("/test")
            .bodyValue(speedTimer)
            .exchange()
            .expectStatus().isOk
            .expectBody<SpeedTimer>().isEqualTo(speedTimer)
    }

    companion object {
        private var speedTimer = SpeedTimer(0, TimerAction.START)
    }
}
