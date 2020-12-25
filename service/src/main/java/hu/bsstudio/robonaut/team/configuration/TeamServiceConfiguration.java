package hu.bsstudio.robonaut.team.configuration;

import hu.bsstudio.robonaut.repository.TeamRepository;
import hu.bsstudio.robonaut.team.BroadcastingTeamService;
import hu.bsstudio.robonaut.team.DefaultTeamService;
import hu.bsstudio.robonaut.team.TeamService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamServiceConfiguration {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TeamRepository teamRepository;

    @Bean
    public TeamService teamService(final TeamService defaultTeamService) {
        return new BroadcastingTeamService(rabbitTemplate, defaultTeamService);
    }

    @Bean
    public TeamService defaultTeamService() {
        return new DefaultTeamService(teamRepository);
    }
}
