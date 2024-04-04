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
    ): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/speed/timer", updateSpeedTimerHandler)
            .POST("/api/speed/lap", speedRaceLapHandler)
            .POST("/api/speed/result/junior", juniorSpeedRaceResultHandler)
            .POST("/api/speed/result/senior", seniorSpeedRaceResultHandler)
            .build()
    }

    @Bean
    fun updateSpeedTimerHandler() = UpdateSpeedTimerHandler(speedTimerService)

    @Bean
    fun speedRaceLapHandler() = SpeedRaceLapHandler(speedRaceService)

    @Bean
    fun juniorSpeedRaceResultHandler() = JuniorSpeedRaceResultHandler(speedRaceService)

    @Bean
    fun seniorSpeedRaceResultHandler() = SeniorSpeedRaceResultHandler(speedRaceService)
}
