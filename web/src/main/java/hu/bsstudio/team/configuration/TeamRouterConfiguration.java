package hu.bsstudio.team.configuration;

import hu.bsstudio.security.RobonAuthFilter;
import hu.bsstudio.team.CreateTeamHandler;
import hu.bsstudio.team.ReadAllTeamHandler;
import hu.bsstudio.team.TeamService;
import hu.bsstudio.team.UpdateTeamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TeamRouterConfiguration {

    @Autowired
    private TeamService teamService;

    @Autowired
    private RobonAuthFilter robonAuthFilter;

    @Bean
    public RouterFunction<ServerResponse> teamRouterFunction(final CreateTeamHandler createTeamHandler,
                                                             final UpdateTeamHandler updateTeamHandler,
                                                             final ReadAllTeamHandler readAllTeamHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/team", createTeamHandler)
            .PUT("/api/team", updateTeamHandler)
            .GET("/api/team", readAllTeamHandler)
            .build();
    }

    @Bean
    public CreateTeamHandler createTeamHandler() {
        return new CreateTeamHandler(teamService);
    }

    @Bean
    public ReadAllTeamHandler readAllTeamHandler() {
        return new ReadAllTeamHandler(teamService);
    }

    @Bean
    public UpdateTeamHandler updateTeamHandler() {
        return new UpdateTeamHandler(teamService);
    }
}
