package hu.bsstudio.race.speed.configuration;

import hu.bsstudio.race.speed.DefaultSpeedRaceService;
import hu.bsstudio.race.speed.SpeedRaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeedRaceServiceConfiguration {

    @Bean
    public SpeedRaceService speedRaceService() {
        return new DefaultSpeedRaceService();
    }
}
