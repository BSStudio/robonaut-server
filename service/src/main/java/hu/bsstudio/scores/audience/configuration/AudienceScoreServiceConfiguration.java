package hu.bsstudio.scores.audience.configuration;

import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.scores.audience.AudienceScoreService;
import hu.bsstudio.scores.audience.DefaultAudienceScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudienceScoreServiceConfiguration {

    @Autowired
    private TeamRepository teamRepository;

    @Bean
    public AudienceScoreService audienceScoreService() {
        return new DefaultAudienceScoreService(teamRepository);
    }
}
