package hu.bsstudio.robonaut.scores.audience.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.audience.AudienceScoreService;
import hu.bsstudio.robonaut.scores.audience.BroadcastingAudienceScoreService;
import hu.bsstudio.robonaut.scores.audience.DefaultAudienceScoreService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudienceScoreServiceConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TeamRepository teamRepository;

    @Bean
    public AudienceScoreService audienceScoreService(final AudienceScoreService defaultAudienceScoreService) {
        return new BroadcastingAudienceScoreService(rabbitTemplate, defaultAudienceScoreService);
    }

    @Bean
    public AudienceScoreService defaultAudienceScoreService() {
        return new DefaultAudienceScoreService(teamRepository);
    }
}
