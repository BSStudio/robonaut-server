package hu.bsstudio.robonaut.speed.configuration;

import hu.bsstudio.robonaut.race.speed.SpeedRaceService;
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService;
import hu.bsstudio.robonaut.security.RobonAuthFilter;
import hu.bsstudio.robonaut.speed.JuniorSpeedRaceResultHandler;
import hu.bsstudio.robonaut.speed.SeniorSpeedRaceResultHandler;
import hu.bsstudio.robonaut.speed.SpeedRaceLapHandler;
import hu.bsstudio.robonaut.speed.UpdateSpeedTimerHandler;
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
    public RouterFunction<ServerResponse> speedRaceRouterFunction(final UpdateSpeedTimerHandler updateSpeedTimerHandler,
                                                                  final SpeedRaceLapHandler speedRaceLapHandler,
                                                                  final JuniorSpeedRaceResultHandler juniorSpeedRaceResultHandler,
                                                                  final SeniorSpeedRaceResultHandler seniorSpeedRaceResultHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/speed/timer", updateSpeedTimerHandler)
            .POST("/api/speed/lap", speedRaceLapHandler)
            .POST("/api/speed/result/junior", juniorSpeedRaceResultHandler)
            .POST("/api/speed/result/senior", seniorSpeedRaceResultHandler)
            .build();
    }

    @Bean
    public UpdateSpeedTimerHandler updateSpeedTimerHandler() {
        return new UpdateSpeedTimerHandler(speedTimerService);
    }

    @Bean
    public SpeedRaceLapHandler speedRaceLapHandler() {
        return new SpeedRaceLapHandler(speedRaceService);
    }

    @Bean
    public JuniorSpeedRaceResultHandler juniorSpeedRaceResultHandler() {
        return new JuniorSpeedRaceResultHandler(speedRaceService);
    }

    @Bean
    public SeniorSpeedRaceResultHandler seniorSpeedRaceResultHandler() {
        return new SeniorSpeedRaceResultHandler(speedRaceService);
    }
}
