package hu.bsstudio.scores.endresult.configuration;

import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.scores.endresult.DefaultEndResultService;
import hu.bsstudio.scores.endresult.EndResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndResultServiceConfiguration {

    @Autowired
    private TeamRepository teamRepository;

    @Bean
    public EndResultService endResultService() {
        return new DefaultEndResultService(teamRepository);
    }
}
