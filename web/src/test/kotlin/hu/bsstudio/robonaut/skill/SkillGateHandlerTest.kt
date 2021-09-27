package hu.bsstudio.robonaut.skill

import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.race.skill.model.GateInformation
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

internal class SkillGateHandlerTest {

    @MockK
    private lateinit var mockService: SkillRaceService

    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val underTest = SkillGateHandler(mockService)
        val routerFunction = RouterFunctions.route()
            .POST("/test", underTest).build()
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    internal fun `should return DetailedTeam with OK status`() {
        val gateInfo = GateInformation(0, 0, 0, 0, 0)
        val detailedTeam = DetailedTeam()
        every { mockService.updateSkillRaceResultOnGate(gateInfo) } returns Mono.just(detailedTeam)

        webTestClient.post().uri("/test").bodyValue(gateInfo).exchange()
            .expectStatus().isOk
            .expectBody<DetailedTeam>().isEqualTo(detailedTeam)
    }
}
