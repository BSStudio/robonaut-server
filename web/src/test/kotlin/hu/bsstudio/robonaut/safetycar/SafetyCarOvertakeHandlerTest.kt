package hu.bsstudio.robonaut.safetycar

import hu.bsstudio.robonaut.safetycar.model.SafetyCarOvertakeInformation
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

internal class SafetyCarOvertakeHandlerTest {

    @MockK
    private lateinit var mockService: SafetyCarService
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val underTest = SafetyCarOvertakeHandler(mockService)
        val routerFunction = RouterFunctions.route()
            .POST(URI, underTest).build()
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    internal fun `should return DetailedTeam with OK status`() {
        val safetyCarOvertakeInformation = SafetyCarOvertakeInformation(0, 0)
        val detailedTeam = DetailedTeam()
        every { mockService.safetyCarWasOvertaken(safetyCarOvertakeInformation) } returns Mono.just(detailedTeam)
        webTestClient.post().uri(URI).bodyValue(safetyCarOvertakeInformation).exchange()
            .expectStatus().isOk
            .expectBody<DetailedTeam>().isEqualTo(detailedTeam)
    }

    companion object {
        private const val URI = "/test"
    }
}
