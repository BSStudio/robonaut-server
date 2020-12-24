package hu.bsstudio.robonaut.team.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.DefaultTeamService;
import hu.bsstudio.robonaut.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamServiceConfiguration {

    @Autowired
    private TeamRepository teamRepository;

    @Bean
    public TeamService teamService() {
        return new DefaultTeamService(teamRepository);
    }
}
