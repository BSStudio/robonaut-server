package hu.bsstudio.race.speed.configuration;

import hu.bsstudio.race.speed.DefaultSpeedRaceService;
import hu.bsstudio.race.speed.SpeedRaceService;
import hu.bsstudio.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeedRaceServiceConfiguration {

    @Autowired
    private TeamRepository repository;

    @Bean
    public SpeedRaceService speedRaceService() {
        return new DefaultSpeedRaceService(repository);
    }
}
