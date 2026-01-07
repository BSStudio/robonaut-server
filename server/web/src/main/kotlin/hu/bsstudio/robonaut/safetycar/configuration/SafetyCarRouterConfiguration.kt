package hu.bsstudio.robonaut.safetycar.configuration

import hu.bsstudio.robonaut.safetycar.SafetyCarFollowHandler
import hu.bsstudio.robonaut.safetycar.SafetyCarOvertakeHandler
import hu.bsstudio.robonaut.safetycar.SafetyCarService
import hu.bsstudio.robonaut.security.RobonAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class SafetyCarRouterConfiguration(
  private val robonAuthFilter: RobonAuthFilter,
  private val safetyCarService: SafetyCarService,
) {
  @Bean
  fun safetyCarRouterFunction(
    safetyCarFollowHandler: SafetyCarFollowHandler,
    safetyCarOvertakeHandler: SafetyCarOvertakeHandler,
  ): RouterFunction<ServerResponse> =
    RouterFunctions
      .route()
      .filter(robonAuthFilter)
      .path("/api/speed/safetyCar") { builder ->
        builder
          .POST("/follow", safetyCarFollowHandler)
          .POST("/overtake", safetyCarOvertakeHandler)
          .build()
      }.build()

  @Bean
  fun safetyCarFollowHandler() = SafetyCarFollowHandler(safetyCarService)

  @Bean
  fun safetyCarOvertakeHandler() = SafetyCarOvertakeHandler(safetyCarService)
}
