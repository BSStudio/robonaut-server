package hu.bsstudio.robonaut.speed.configuration;

import hu.bsstudio.robonaut.race.speed.SpeedRaceService;
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService;
import hu.bsstudio.robonaut.security.RobonAuthFilter;
import hu.bsstudio.robonaut.speed.SpeedRaceLapHandler;
import hu.bsstudio.robonaut.speed.StartSpeedTimerHandler;
import hu.bsstudio.robonaut.speed.StopSpeedTimerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class SpeedRaceRouterConfiguration {

    @Autowired
    private RobonAuthFilter robonAuthFilter;

    @Autowired
    private SpeedTimerService speedTimerService;

    @Autowired
    private SpeedRaceService speedRaceService;

    @Bean
    public RouterFunction<ServerResponse> speedRaceRouterFunction(final StartSpeedTimerHandler startSpeedTimerHandler,
                                                                  final StopSpeedTimerHandler stopSpeedTimerHandler,
                                                                  final SpeedRaceLapHandler speedRaceLapHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/speed/timer/start", startSpeedTimerHandler)
            .POST("/api/speed/timer/stop", stopSpeedTimerHandler)
            .POST("/api/speed/lap", speedRaceLapHandler)
            .build();
    }

    @Bean
    public StartSpeedTimerHandler startSpeedTimerHandler() {
        return new StartSpeedTimerHandler(speedTimerService);
    }

    @Bean
    public StopSpeedTimerHandler stopSpeedTimerHandler() {
        return new StopSpeedTimerHandler(speedTimerService);
    }

    @Bean
    public SpeedRaceLapHandler speedRaceLapHandler() {
        return new SpeedRaceLapHandler(speedRaceService);
    }
}
