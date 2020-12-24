package hu.bsstudio.robonaut.scores.endresult.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.scores.endresult.DefaultEndResultService;
import hu.bsstudio.robonaut.scores.endresult.EndResultService;
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
