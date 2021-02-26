package hu.bsstudio.robonaut.scores.configuration;

import hu.bsstudio.robonaut.scores.AudienceScoreHandler;
import hu.bsstudio.robonaut.scores.JuniorEndResultHandler;
import hu.bsstudio.robonaut.scores.QualificationScoreHandler;
import hu.bsstudio.robonaut.scores.SeniorEndResultHandler;
import hu.bsstudio.robonaut.scores.audience.AudienceScoreService;
import hu.bsstudio.robonaut.scores.endresult.EndResultService;
import hu.bsstudio.robonaut.scores.qualification.QualificationScoreService;
import hu.bsstudio.robonaut.security.RobonAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ScoreRouterConfiguration {

    @Autowired
    private RobonAuthFilter robonAuthFilter;
    @Autowired
    private QualificationScoreService qualificationScoreService;
    @Autowired
    private AudienceScoreService audienceScoreService;
    @Autowired
    private EndResultService endResultService;

    @Bean
    public RouterFunction<ServerResponse> scoreRouterFunction(final QualificationScoreHandler qualificationScoreHandler,
                                                              final AudienceScoreHandler audienceScoreHandler,
                                                              final SeniorEndResultHandler seniorEndResultHandler,
                                                              final JuniorEndResultHandler juniorEndResultHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/scores/qualification", qualificationScoreHandler)
            .POST("/api/scores/audience", audienceScoreHandler)
            .POST("/api/scores/endResult/senior", seniorEndResultHandler)
            .POST("/api/scores/endResult/junior", juniorEndResultHandler)
            .build();
    }

    @Bean
    public QualificationScoreHandler qualificationScoreHandler() {
        return new QualificationScoreHandler(qualificationScoreService);
    }

    @Bean
    public AudienceScoreHandler audienceScoreHandler() {
        return new AudienceScoreHandler(audienceScoreService);
    }

    @Bean
    public SeniorEndResultHandler seniorEndResultHandler() {
        return new SeniorEndResultHandler(endResultService);
    }

    @Bean
    public JuniorEndResultHandler juniorEndResultHandler() {
        return new JuniorEndResultHandler(endResultService);
    }
}
