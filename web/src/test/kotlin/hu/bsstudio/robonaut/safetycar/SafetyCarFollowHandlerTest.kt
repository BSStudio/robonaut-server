package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarFollowInformation
import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.RouterFunctions
import reactor.core.publisher.Mono
import java.time.Duration

internal class SafetyCarFollowHandlerTest {
    @MockK
    private lateinit var mockService: SafetyCarService
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val underTest = SafetyCarFollowHandler(mockService)
        val routerFunction =
            RouterFunctions.route()
                .POST("/test", underTest).build()
        webTestClient =
            WebTestClient
                .bindToRouterFunction(routerFunction)
                .build()
                .mutate()
                .responseTimeout(Duration.ofMinutes(1L))
                .build()
    }

    @Test
    internal fun `should return DetailedTeam with OK status`() {
        val safetyCarFollowInformation = SafetyCarFollowInformation(0, true)
        val detailedTeam = DetailedTeam()
        every { mockService.safetyCarWasFollowed(safetyCarFollowInformation) } returns Mono.just(detailedTeam)

        webTestClient.post().uri("/test").bodyValue(safetyCarFollowInformation).exchange()
            .expectStatus().isOk
            .expectBody<DetailedTeam>().isEqualTo(detailedTeam)
    }
}
