package hu.bsstudio.robonaut.team.configuration;

import hu.bsstudio.robonaut.team.TeamService;
import hu.bsstudio.robonaut.security.RobonAuthFilter;
import hu.bsstudio.robonaut.team.CreateTeamHandler;
import hu.bsstudio.robonaut.team.ReadAllTeamHandler;
import hu.bsstudio.robonaut.team.UpdateTeamHandler;
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
