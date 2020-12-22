package hu.bsstudio.skill.configuration;

import hu.bsstudio.race.skill.SkillRaceService;
import hu.bsstudio.race.skill.timer.SkillTimerService;
import hu.bsstudio.security.RobonAuthFilter;
import hu.bsstudio.skill.SkillGateHandler;
import hu.bsstudio.skill.SkillRaceResultHandler;
import hu.bsstudio.skill.StartSkillTimerHandler;
import hu.bsstudio.skill.StopSkillTimerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SkillRouterConfiguration {

    @Autowired
    private RobonAuthFilter robonAuthFilter;
    @Autowired
    private SkillTimerService skillTimerService;
    @Autowired
    private SkillRaceService skillRaceService;

    @Bean
    public RouterFunction<ServerResponse> skillRouterFunction(final StartSkillTimerHandler startSkillTimerHandler,
                                                              final StopSkillTimerHandler stopSkillTimerHandler,
                                                              final SkillGateHandler skillGateHandler,
                                                              final SkillRaceResultHandler skillRaceResultHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/skill/timer/start", startSkillTimerHandler)
            .POST("/api/skill/timer/stop", stopSkillTimerHandler)
            .POST("/api/skill/gate", skillGateHandler)
            .POST("/api/skill/result", skillRaceResultHandler)
            .build();
    }

    @Bean
    public StartSkillTimerHandler startSkillTimerHandler() {
        return new StartSkillTimerHandler(skillTimerService);
    }

    @Bean
    public StopSkillTimerHandler stopSkillTimerHandler() {
        return new StopSkillTimerHandler(skillTimerService);
    }

    @Bean
    public SkillGateHandler skillGateHandler() {
        return new SkillGateHandler(skillRaceService);
    }

    @Bean
    public SkillRaceResultHandler skillRaceResultHandler() {
        return new SkillRaceResultHandler(skillRaceService);
    }
}
