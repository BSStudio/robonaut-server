package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.RouterFunctions
import reactor.core.publisher.Flux

internal class ReadAllTeamHandlerTest {
    @MockK
    private lateinit var mockService: TeamService
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val underTest = ReadAllTeamHandler(mockService)
        val routerFunction =
            RouterFunctions.route()
                .POST("/test", underTest).build()
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    internal fun `should return DetailedTeam with OK status`() {
        val detailedTeam = DetailedTeam()
        every { mockService.findAllTeam() } returns Flux.just(detailedTeam)

        webTestClient.post().uri("/test").exchange()
            .expectStatus().isOk
            .expectBody<List<DetailedTeam>>()
            .isEqualTo(listOf(detailedTeam))
    }
}
