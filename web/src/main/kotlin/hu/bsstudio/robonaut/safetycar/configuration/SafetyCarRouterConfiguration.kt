package hu.bsstudio.robonaut.safetycar.configuration

import hu.bsstudio.robonaut.safetycar.SafetyCarFollowHandler
import hu.bsstudio.robonaut.safetycar.SafetyCarOvertakeHandler
import hu.bsstudio.robonaut.safetycar.SafetyCarService
import hu.bsstudio.robonaut.security.RobonAuthFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class SafetyCarRouterConfiguration(
    @Autowired private val robonAuthFilter: RobonAuthFilter,
    @Autowired private val safetyCarService: SafetyCarService
) {

    @Bean
    fun safetyCarRouterFunction(
        safetyCarFollowHandler: SafetyCarFollowHandler,
        safetyCarOvertakeHandler: SafetyCarOvertakeHandler
    ): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/speed/safetyCar/follow", safetyCarFollowHandler)
            .POST("/api/speed/safetyCar/overtake", safetyCarOvertakeHandler)
            .build()
    }

    @Bean
    fun safetyCarFollowHandler(): SafetyCarFollowHandler = SafetyCarFollowHandler(safetyCarService)

    @Bean
    fun safetyCarOvertakeHandler(): SafetyCarOvertakeHandler = SafetyCarOvertakeHandler(safetyCarService)
}
