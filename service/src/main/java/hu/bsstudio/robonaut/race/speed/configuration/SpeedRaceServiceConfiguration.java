package hu.bsstudio.robonaut.race.speed.configuration;

import hu.bsstudio.robonaut.race.speed.BroadcastingSpeedRaceService;
import hu.bsstudio.robonaut.race.speed.DefaultSpeedRaceService;
import hu.bsstudio.robonaut.race.speed.SpeedRaceService;
import hu.bsstudio.robonaut.repository.TeamRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeedRaceServiceConfiguration {

    @Autowired
    private TeamRepository repository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public SpeedRaceService speedRaceService(final SpeedRaceService defaultSpeedRaceService) {
        return new BroadcastingSpeedRaceService(rabbitTemplate, defaultSpeedRaceService);
    }

    @Bean
    public SpeedRaceService defaultSpeedRaceService() {
        return new DefaultSpeedRaceService(repository);
    }
}
