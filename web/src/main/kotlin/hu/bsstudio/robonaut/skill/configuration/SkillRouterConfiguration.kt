package hu.bsstudio.robonaut.skill.configuration

import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.skill.SkillGateHandler
import hu.bsstudio.robonaut.skill.SkillRaceResultHandler
import hu.bsstudio.robonaut.skill.UpdateSkillTimerHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class SkillRouterConfiguration(
    @Autowired private val robonAuthFilter: RobonAuthFilter,
    @Autowired private val skillTimerService: SkillTimerService,
    @Autowired private val skillRaceService: SkillRaceService
) {

    @Bean
    fun skillRouterFunction(
        updateSkillTimerHandler: UpdateSkillTimerHandler,
        skillGateHandler: SkillGateHandler,
        skillRaceResultHandler: SkillRaceResultHandler
    ): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/skill/timer", updateSkillTimerHandler)
            .POST("/api/skill/gate", skillGateHandler)
            .POST("/api/skill/result", skillRaceResultHandler)
            .build()
    }

    @Bean
    fun updateSkillTimerHandler() = UpdateSkillTimerHandler(skillTimerService)

    @Bean
    fun skillGateHandler() = SkillGateHandler(skillRaceService)

    @Bean
    fun skillRaceResultHandler() = SkillRaceResultHandler(skillRaceService)
}
