package hu.bsstudio.skill.configuration;

import hu.bsstudio.race.skill.timer.SkillTimerService;
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
    private SkillTimerService skillTimerService;

    @Bean
    public RouterFunction<ServerResponse> skillRouterFunction(final StartSkillTimerHandler startSkillTimerHandler,
                                                              final StopSkillTimerHandler stopSkillTimerHandler) {
        return RouterFunctions.route()
            .POST("/api/skill/timer/start", startSkillTimerHandler)
            .POST("/api/skill/timer/stop", stopSkillTimerHandler)
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
}
