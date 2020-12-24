package hu.bsstudio.robonaut.race.speed.timer.configuration;

import hu.bsstudio.robonaut.race.speed.timer.DefaultSpeedTimerService;
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeedTimerServiceConfiguration {

    @Bean
    public SpeedTimerService speedTimerService() {
        return new DefaultSpeedTimerService();
    }
}
