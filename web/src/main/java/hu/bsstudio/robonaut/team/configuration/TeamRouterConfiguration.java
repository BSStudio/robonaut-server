package hu.bsstudio.robonaut.team.configuration;

import hu.bsstudio.robonaut.security.RobonAuthFilter;
import hu.bsstudio.robonaut.team.AdminUpdateTeamHandler;
import hu.bsstudio.robonaut.team.CreateTeamHandler;
import hu.bsstudio.robonaut.team.ReadAllTeamHandler;
import hu.bsstudio.robonaut.team.TeamService;
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
                                                             final ReadAllTeamHandler readAllTeamHandler,
                                                             final AdminUpdateTeamHandler adminUpdateTeamHandler,
                                                             final UpdateTeamHandler updateTeamHandler) {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/team", createTeamHandler)
            .GET("/api/team", readAllTeamHandler)
            .PUT("/api/team", updateTeamHandler)
            .PUT("/api/admin/team", adminUpdateTeamHandler)
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

    @Bean
    public AdminUpdateTeamHandler adminUpdateTeamHandler() {
        return new AdminUpdateTeamHandler(teamService);
    }
}
