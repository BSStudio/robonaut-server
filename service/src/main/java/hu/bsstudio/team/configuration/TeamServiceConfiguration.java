package hu.bsstudio.team.configuration;

import hu.bsstudio.repository.TeamRepository;
import hu.bsstudio.team.DefaultTeamService;
import hu.bsstudio.team.TeamService;
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
