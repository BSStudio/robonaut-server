package hu.bsstudio.speed.configuration;

import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.security.RobonAuthFilter;
import hu.bsstudio.speed.StartTimerHandler;
import hu.bsstudio.speed.StopTimerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SpeedRaceRouterConfiguration {

    @Autowired
    private RobonAuthFilter authFilter;

    @Autowired
    private SpeedRaceService service;

    @Bean
    public RouterFunction<ServerResponse> speedRaceRouterFunction(final StartTimerHandler startTimerHandler,
                                                                  final StopTimerHandler stopTimerHandler) {
        return RouterFunctions.route()
            .filter(authFilter)
            .POST("/api/speed/timer/start", startTimerHandler)
            .POST("/api/speed/timer/stop", stopTimerHandler)
            .build();
    }

    @Bean
    public StartTimerHandler startTimerHandler() {
        return new StartTimerHandler(service);
    }

    @Bean
    public StopTimerHandler stopTimerHandler() {
        return new StopTimerHandler(service);
    }
}
