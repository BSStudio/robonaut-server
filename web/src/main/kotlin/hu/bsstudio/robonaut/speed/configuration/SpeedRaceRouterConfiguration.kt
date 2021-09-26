package hu.bsstudio.robonaut.speed.configuration

import org.springframework.beans.factory.annotation.Autowired
import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService
import hu.bsstudio.robonaut.race.speed.SpeedRaceService
import hu.bsstudio.robonaut.speed.UpdateSpeedTimerHandler
import hu.bsstudio.robonaut.speed.SpeedRaceLapHandler
import hu.bsstudio.robonaut.speed.JuniorSpeedRaceResultHandler
import hu.bsstudio.robonaut.speed.SeniorSpeedRaceResultHandler
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.RouterFunctions
import lombok.RequiredArgsConstructor
import org.springframework.web.reactive.function.server.HandlerFunction
import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceScore
import hu.bsstudio.robonaut.race.speed.timer.model.SpeedTimer
import hu.bsstudio.robonaut.race.speed.model.SpeedRaceResult
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpeedRaceRouterConfiguration(
    @Autowired private val robonAuthFilter: RobonAuthFilter,
    @Autowired private val speedTimerService: SpeedTimerService,
    @Autowired private val speedRaceService: SpeedRaceService
) {

    @Bean
    fun speedRaceRouterFunction(
        updateSpeedTimerHandler: UpdateSpeedTimerHandler,
        speedRaceLapHandler: SpeedRaceLapHandler,
        juniorSpeedRaceResultHandler: JuniorSpeedRaceResultHandler,
        seniorSpeedRaceResultHandler: SeniorSpeedRaceResultHandler
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
    fun updateSpeedTimerHandler(): UpdateSpeedTimerHandler = UpdateSpeedTimerHandler(speedTimerService)

    @Bean
    fun speedRaceLapHandler(): SpeedRaceLapHandler = SpeedRaceLapHandler(speedRaceService)

    @Bean
    fun juniorSpeedRaceResultHandler(): JuniorSpeedRaceResultHandler = JuniorSpeedRaceResultHandler(speedRaceService)

    @Bean
    fun seniorSpeedRaceResultHandler(): SeniorSpeedRaceResultHandler = SeniorSpeedRaceResultHandler(speedRaceService)
}
