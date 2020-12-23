package hu.bsstudio.speed.configuration;

import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.race.speed.timer.SpeedTimerService;
import hu.bsstudio.security.RobonAuthFilter;
import hu.bsstudio.speed.SpeedRaceLapHandler;
import hu.bsstudio.speed.SpeedRaceResultHandler;
import hu.bsstudio.speed.StartSpeedTimerHandler;
import hu.bsstudio.speed.StopSpeedTimerHandler;
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
                                                                  final SpeedRaceLapHandler speedRaceLapHandler,
                                                                  final SpeedRaceResultHandler speedRaceResultHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/speed/timer/start", startSpeedTimerHandler)
            .POST("/api/speed/timer/stop", stopSpeedTimerHandler)
            .POST("/api/speed/lap", speedRaceLapHandler)
            .POST("/api/speed/result", speedRaceResultHandler)
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

    @Bean
    public SpeedRaceResultHandler speedRaceResultHandler() {
        return new SpeedRaceResultHandler(speedRaceService);
    }
}
