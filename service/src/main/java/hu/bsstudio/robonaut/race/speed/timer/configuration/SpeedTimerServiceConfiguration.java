package hu.bsstudio.robonaut.race.speed.timer.configuration;

import hu.bsstudio.robonaut.race.speed.timer.BroadcastingSpeedTimerService;
import hu.bsstudio.robonaut.race.speed.timer.DefaultSpeedTimerService;
import hu.bsstudio.robonaut.race.speed.timer.SpeedTimerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpeedTimerServiceConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public SpeedTimerService speedTimerService(final SpeedTimerService defaultSpeedTimerService) {
        return new BroadcastingSpeedTimerService(rabbitTemplate, defaultSpeedTimerService);
    }

    @Bean
    public SpeedTimerService defaultSpeedTimerService() {
        return new DefaultSpeedTimerService();
    }
}
