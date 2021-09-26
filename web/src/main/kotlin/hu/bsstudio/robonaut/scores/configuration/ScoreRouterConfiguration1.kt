package hu.bsstudio.robonaut.scores.configuration

import hu.bsstudio.robonaut.scores.AudienceScoreHandler
import hu.bsstudio.robonaut.scores.JuniorEndResultHandler
import hu.bsstudio.robonaut.scores.QualificationScoreHandler
import hu.bsstudio.robonaut.scores.SeniorEndResultHandler
import hu.bsstudio.robonaut.scores.audience.AudienceScoreService
import hu.bsstudio.robonaut.scores.endresult.EndResultService
import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService
import hu.bsstudio.robonaut.security.RobonAuthFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ScoreRouterConfiguration(
    @Autowired private val robonAuthFilter: RobonAuthFilter,
    @Autowired private val qualificationScoreService: QualificationScoreService,
    @Autowired private val audienceScoreService: AudienceScoreService,
    @Autowired private val endResultService: EndResultService
) {

    @Bean
    fun scoreRouterFunction(
        qualificationScoreHandler: QualificationScoreHandler,
        audienceScoreHandler: AudienceScoreHandler,
        seniorEndResultHandler: SeniorEndResultHandler,
        juniorEndResultHandler: JuniorEndResultHandler
    ): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/scores/qualification", qualificationScoreHandler)
            .POST("/api/scores/audience", audienceScoreHandler)
            .POST("/api/scores/endResult/senior", seniorEndResultHandler)
            .POST("/api/scores/endResult/junior", juniorEndResultHandler)
            .build()
    }

    @Bean
    fun qualificationScoreHandler(): QualificationScoreHandler = QualificationScoreHandler(qualificationScoreService)

    @Bean
    fun audienceScoreHandler(): AudienceScoreHandler = AudienceScoreHandler(audienceScoreService)

    @Bean
    fun seniorEndResultHandler(): SeniorEndResultHandler = SeniorEndResultHandler(endResultService)

    @Bean
    fun juniorEndResultHandler(): JuniorEndResultHandler = JuniorEndResultHandler(endResultService)
}
