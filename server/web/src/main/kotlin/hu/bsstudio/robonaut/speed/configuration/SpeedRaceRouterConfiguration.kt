package hu.bsstudio.robonaut.speed.configuration

import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService
import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.speed.JuniorSpeedRaceResultHandler
import hu.bsstudio.robonaut.speed.SeniorSpeedRaceResultHandler
import hu.bsstudio.robonaut.speed.SpeedRaceLapHandler
import hu.bsstudio.robonaut.speed.UpdateSpeedTimerHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class SpeedRaceRouterConfiguration(
  private val robonAuthFilter: RobonAuthFilter,
  private val speedTimerService: SpeedTimerService,
  private val speedRaceService: SpeedRaceService,
) {
  @Bean
  fun speedRaceRouterFunction(
    updateSpeedTimerHandler: UpdateSpeedTimerHandler,
    speedRaceLapHandler: SpeedRaceLapHandler,
    juniorSpeedRaceResultHandler: JuniorSpeedRaceResultHandler,
    seniorSpeedRaceResultHandler: SeniorSpeedRaceResultHandler,
  ): RouterFunction<ServerResponse> =
    RouterFunctions
      .route()
      .filter(robonAuthFilter)
      .path("/api/speed/") { builder ->
        builder
          .POST("/timer", updateSpeedTimerHandler)
          .POST("/lap", speedRaceLapHandler)
          .POST("/result/junior", juniorSpeedRaceResultHandler)
          .POST("/result/senior", seniorSpeedRaceResultHandler)
          .build()
      }.build()

  @Bean
  fun updateSpeedTimerHandler() = UpdateSpeedTimerHandler(speedTimerService)

  @Bean
  fun speedRaceLapHandler() = SpeedRaceLapHandler(speedRaceService)

  @Bean
  fun juniorSpeedRaceResultHandler() = JuniorSpeedRaceResultHandler(speedRaceService)

  @Bean
  fun seniorSpeedRaceResultHandler() = SeniorSpeedRaceResultHandler(speedRaceService)
}
