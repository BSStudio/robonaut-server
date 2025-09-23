package hu.bsstudio.robonaut.speed

import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
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

internal class JuniorSpeedRaceResultHandlerTest {
  @MockK
  private lateinit var mockService: SpeedRaceService
  private lateinit var webTestClient: WebTestClient

  @BeforeEach
  internal fun setUp() {
    MockKAnnotations.init(this)
    val underTest = JuniorSpeedRaceResultHandler(mockService)
    val routerFunction =
      RouterFunctions
        .route()
        .POST("/test", underTest)
        .build()
    webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()
  }

  @Test
  internal fun `should return DetailedTeam with OK status`() {
    val speedRaceResult = SpeedRaceResult(0, 0, 0, emptyList())
    val detailedTeam = DetailedTeam()
    every { mockService.updateSpeedRaceJunior(speedRaceResult) } returns Mono.just(detailedTeam)

    webTestClient
      .post()
      .uri("/test")
      .bodyValue(speedRaceResult)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody<DetailedTeam>()
      .isEqualTo(detailedTeam)
  }
}
