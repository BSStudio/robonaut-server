package hu.bsstudio.robonaut.scores.endresult.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.endresult.BroadcastingEndResultService;
import hu.bsstudio.robonaut.scores.endresult.DefaultEndResultService;
import hu.bsstudio.robonaut.scores.endresult.EndResultService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndResultServiceConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TeamRepository teamRepository;

    @Bean
    public EndResultService endResultService(final EndResultService defaultEndResultService) {
        return new BroadcastingEndResultService(rabbitTemplate, defaultEndResultService);
    }

    @Bean
    public EndResultService defaultEndResultService() {
        return new DefaultEndResultService(teamRepository);
    }
}
