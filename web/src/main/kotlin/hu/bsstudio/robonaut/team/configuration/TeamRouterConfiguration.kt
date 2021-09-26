package hu.bsstudio.robonaut.team.configuration

import org.springframework.beans.factory.annotation.Autowired
import hu.bsstudio.robonaut.team.TeamService
import hu.bsstudio.robonaut.security.RobonAuthFilter
import hu.bsstudio.robonaut.team.CreateTeamHandler
import hu.bsstudio.robonaut.team.ReadAllTeamHandler
import hu.bsstudio.robonaut.team.AdminUpdateTeamHandler
import hu.bsstudio.robonaut.team.UpdateTeamHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.RouterFunctions

@Configuration
class TeamRouterConfiguration(
    @Autowired private val teamService: TeamService,
    @Autowired private val robonAuthFilter: RobonAuthFilter
) {

    @Bean
    fun teamRouterFunction(
        createTeamHandler: CreateTeamHandler,
        readAllTeamHandler: ReadAllTeamHandler,
        adminUpdateTeamHandler: AdminUpdateTeamHandler,
        updateTeamHandler: UpdateTeamHandler
    ): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(robonAuthFilter)
            .POST("/api/team", createTeamHandler)
            .GET("/api/team", readAllTeamHandler)
            .PUT("/api/team", updateTeamHandler)
            .PUT("/api/admin/team", adminUpdateTeamHandler)
            .build()
    }

    @Bean
    fun createTeamHandler(): CreateTeamHandler = CreateTeamHandler(teamService)

    @Bean
    fun readAllTeamHandler(): ReadAllTeamHandler = ReadAllTeamHandler(teamService)

    @Bean
    fun updateTeamHandler(): UpdateTeamHandler = UpdateTeamHandler(teamService)

    @Bean
    fun adminUpdateTeamHandler(): AdminUpdateTeamHandler = AdminUpdateTeamHandler(teamService)
}
