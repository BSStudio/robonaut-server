package hu.bsstudio.scores.configuration;

import hu.bsstudio.scores.AudienceScoreHandler;
import hu.bsstudio.scores.EndResultHandler;
import hu.bsstudio.scores.QualificationScoreHandler;
import hu.bsstudio.scores.audience.AudienceScoreService;
import hu.bsstudio.scores.endresult.EndResultService;
import hu.bsstudio.scores.qualification.QualificationScoreService;
import hu.bsstudio.security.RobonAuthFilter;
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
                                                              final EndResultHandler endResultHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/scores/qualification", qualificationScoreHandler)
            .POST("/api/scores/audience", audienceScoreHandler)
            .POST("/api/scores/endResult", endResultHandler)
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
    public EndResultHandler endResultHandler() {
        return new EndResultHandler(endResultService);
    }
}
