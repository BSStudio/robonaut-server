package hu.bsstudio.race.speed.timer.configuration;

import hu.bsstudio.race.speed.timer.DefaultSpeedTimerService;
import hu.bsstudio.race.speed.timer.SpeedTimerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeedRaceServiceConfiguration {

    @Bean
    public SpeedTimerService speedRaceService() {
        return new DefaultSpeedTimerService();
    }
}
