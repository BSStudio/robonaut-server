package hu.bsstudio.robonaut.skill.configuration

import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.skill.SkillGateHandler
import hu.bsstudio.robonaut.skill.SkillRaceResultHandler
import hu.bsstudio.robonaut.skill.UpdateSkillTimerHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class SkillRouterConfiguration(
  private val robonAuthFilter: RobonAuthFilter,
  private val skillTimerService: SkillTimerService,
  private val skillRaceService: SkillRaceService,
) {
  @Bean
  fun skillRouterFunction(
    updateSkillTimerHandler: UpdateSkillTimerHandler,
    skillGateHandler: SkillGateHandler,
    skillRaceResultHandler: SkillRaceResultHandler,
  ): RouterFunction<ServerResponse> =
    RouterFunctions
      .route()
      .filter(robonAuthFilter)
      .path("/api/skill") { builder ->
        builder
          .POST("/timer", updateSkillTimerHandler)
          .POST("/gate", skillGateHandler)
          .POST("/result", skillRaceResultHandler)
          .build()
      }.build()

  @Bean
  fun updateSkillTimerHandler() = UpdateSkillTimerHandler(skillTimerService)

  @Bean
  fun skillGateHandler() = SkillGateHandler(skillRaceService)

  @Bean
  fun skillRaceResultHandler() = SkillRaceResultHandler(skillRaceService)
}
