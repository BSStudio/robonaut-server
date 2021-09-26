package hu.bsstudio.robonaut.team

import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.team.model.Team
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.RouterFunctions
import reactor.core.publisher.Mono

internal class UpdateTeamHandlerTest {
    
    @MockK
    private lateinit var mockService: TeamService
    private lateinit var webTestClient: WebTestClient
    
    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        val underTest = UpdateTeamHandler(mockService)
        val routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build()
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    fun `should return DetailedTeam with OK status`() {
        val team = Team.builder().build()
        val detailedTeam = DetailedTeam.builder().build()
        every { mockService.updateTeam(team) } returns Mono.just(detailedTeam)

        webTestClient.post().uri("/test").bodyValue(team).exchange()
            .expectStatus().isOk
            .expectBody<DetailedTeam>().isEqualTo(detailedTeam)
    }
}
