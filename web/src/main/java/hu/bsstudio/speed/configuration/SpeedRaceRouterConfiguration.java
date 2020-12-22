package hu.bsstudio.speed.configuration;

import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.race.speed.timer.SpeedTimerService;
import hu.bsstudio.security.RobonAuthFilter;
import hu.bsstudio.speed.SpeedRaceLapHandler;
import hu.bsstudio.speed.SpeedRaceResultHandler;
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
    private RobonAuthFilter robonAuthFilter;

    @Autowired
    private SpeedTimerService speedTimerService;

    @Autowired
    private SpeedRaceService speedRaceService;

    @Bean
    public RouterFunction<ServerResponse> speedRaceRouterFunction(final StartTimerHandler startTimerHandler,
                                                                  final StopTimerHandler stopTimerHandler,
                                                                  final SpeedRaceLapHandler speedRaceLapHandler,
                                                                  final SpeedRaceResultHandler speedRaceResultHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/speed/timer/start", startTimerHandler)
            .POST("/api/speed/timer/stop", stopTimerHandler)
            .POST("/api/speed/lap", speedRaceLapHandler)
            .POST("/api/speed/result", speedRaceResultHandler)
            .build();
    }

    @Bean
    public StartTimerHandler startTimerHandler() {
        return new StartTimerHandler(speedTimerService);
    }

    @Bean
    public StopTimerHandler stopTimerHandler() {
        return new StopTimerHandler(speedTimerService);
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
