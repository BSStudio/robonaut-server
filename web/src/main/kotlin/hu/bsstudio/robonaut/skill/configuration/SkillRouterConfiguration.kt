package hu.bsstudio.robonaut.skill.configuration

import org.springframework.beans.factory.annotation.Autowired
import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.race.skill.timer.SkillTimerService
import hu.bsstudio.robonaut.race.skill.SkillRaceService
import hu.bsstudio.robonaut.skill.UpdateSkillTimerHandler
import hu.bsstudio.robonaut.skill.SkillGateHandler
import hu.bsstudio.robonaut.skill.SkillRaceResultHandler
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.RouterFunctions
import lombok.RequiredArgsConstructor
import org.springframework.web.reactive.function.server.HandlerFunction
import hu.bsstudio.robonaut.team.model.DetailedTeam
import hu.bsstudio.robonaut.race.skill.model.GateInformation
import hu.bsstudio.robonaut.race.skill.model.SkillRaceResult
import hu.bsstudio.robonaut.race.skill.timer.model.SkillTimer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
    fun updateSkillTimerHandler(): UpdateSkillTimerHandler = UpdateSkillTimerHandler(skillTimerService)

    @Bean
    fun skillGateHandler(): SkillGateHandler = SkillGateHandler(skillRaceService)

    @Bean
    fun skillRaceResultHandler(): SkillRaceResultHandler = SkillRaceResultHandler(skillRaceService)
}
