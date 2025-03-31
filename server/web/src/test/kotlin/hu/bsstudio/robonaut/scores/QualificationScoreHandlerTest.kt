package hu.bsstudio.robonaut.scores

import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService
import hu.bsstudio.robonaut.scores.qualification.model.QualifiedTeam
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

internal class QualificationScoreHandlerTest {
    @MockK
    private lateinit var mockService: QualificationScoreService
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val underTest = QualificationScoreHandler(mockService)
        val routerFunction = RouterFunctions.route().POST("/test", underTest).build()
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
    }

    @Test
    internal fun `should return DetailedTeam with OK status`() {
        val qualifiedTeam = QualifiedTeam(0, 0)
        val detailedTeam = DetailedTeam()
        every { mockService.updateQualificationScore(qualifiedTeam) } returns Mono.just(detailedTeam)
        webTestClient
            .post()
            .uri("/test")
            .bodyValue(qualifiedTeam)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<List<DetailedTeam>>()
            .isEqualTo(listOf(detailedTeam))
    }
}
