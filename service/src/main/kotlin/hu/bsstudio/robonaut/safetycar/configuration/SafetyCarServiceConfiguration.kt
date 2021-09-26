package hu.bsstudio.robonaut.safetycar.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.safetycar.BroadcastingSafetyCarService;
import hu.bsstudio.robonaut.safetycar.DefaultSafetyCarService;
import hu.bsstudio.robonaut.safetycar.SafetyCarService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SafetyCarServiceConfiguration {

    @Autowired
    private TeamRepository repository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public SafetyCarService safetyCarService(final SafetyCarService defaultSafetyCarService) {
        return new BroadcastingSafetyCarService(rabbitTemplate, defaultSafetyCarService);
    }

    @Bean
    public SafetyCarService defaultSafetyCarService() {
        return new DefaultSafetyCarService(repository);
    }
}
